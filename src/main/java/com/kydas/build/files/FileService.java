package com.kydas.build.files;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.ForbiddenException;
import com.kydas.build.core.exceptions.classes.InternalServerError;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.users.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final SecurityContext securityContext;
    private final static Long MAX_TOTAL_USER_FILES_SIZE_BYTES = 5 * 1024 * 1024 * 1024L;
    private final static Long MIN_USABLE_SPACE = 1024 * 1024 * 1024L;

    public List<File> getAllCurrentUserFiles() throws ApiException {
        var user = securityContext.getCurrentUser();
        return fileRepository.findByUserId(user.getId());
    }

    public List<File> getByUser(User user) {
        return fileRepository.findByUserId(user.getId());
    }

    private Map<File.Type, Dimension> getMaxResolutionForType() {
        Map<File.Type, Dimension> maxResolutions = new HashMap<>();
        maxResolutions.put(File.Type.PROFILE_IMAGE, new Dimension(256, 256));
        maxResolutions.put(File.Type.PROJECT_COVER_IMAGE, new Dimension(1920, 1920));
        maxResolutions.put(File.Type.PROJECT_CONTENT_IMAGE, new Dimension(1920, 50000));
        return maxResolutions;
    }

    private BufferedImage resizeImageIfNeeded(BufferedImage originalImage, File.Type type) {
        Map<File.Type, Dimension> maxResolutions = getMaxResolutionForType();
        Dimension maxDimension = maxResolutions.get(type);

        if (maxDimension == null) {
            return originalImage;
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int maxWidth = maxDimension.width;
        int maxHeight = maxDimension.height;

        // Проверяем, нужно ли изменять размер
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            return originalImage;
        }

        // Вычисляем новое разрешение с сохранением соотношения сторон
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scale = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        return outputImage;
    }

    public File uploadFileForCurrentUser(MultipartFile multipartFile, File.Type type) throws ApiException {
        var user = securityContext.getCurrentUser();
        return uploadFileForUser(multipartFile, type, user);
    }

    public File uploadFileForUser(MultipartFile multipartFile, File.Type type, User user) throws ApiException {
        File file = new File();
        file.setId(UUID.randomUUID());
        file.setOriginalFileName(multipartFile.getOriginalFilename());
        file.setSize(multipartFile.getSize());
        file.setUserId(user.getId());
        file.setType(type.name());

        var totalUserFilesSize = fileRepository.getTotalFileSizeByUserId(user.getId());
        if (totalUserFilesSize + multipartFile.getSize() > MAX_TOTAL_USER_FILES_SIZE_BYTES) {
            throw new ForbiddenException().setMessage("Reached max total user files size quota");
        }

        try {
            Path rootLocation = Paths.get("cdn/files");

            var usableSpace = rootLocation.toFile().getUsableSpace();
            if (usableSpace <= MIN_USABLE_SPACE && rootLocation.toFile().exists()) {
                throw new InternalServerError().setMessage("Low disk space: %s".formatted(usableSpace));
            }

            Files.createDirectories(rootLocation);

            String extension = getFileExtension(multipartFile);

            if (fileNeedToConvertInWebp(extension)) {
                Path webpFilePath = rootLocation.resolve("%s".formatted(file.getId()));
                var webpFile = webpFilePath.toFile();
                if (!webpFile.exists()) {
                    webpFile.createNewFile();
                }
                BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
                BufferedImage resizedImage = resizeImageIfNeeded(bufferedImage, type);
//                ImageIO.write(resizedImage, "webp", webpFile);
                saveWebpImageWithQuality(resizedImage, webpFile, 0.9f);
                file.setSize(webpFile.length());
            } else {
                Files.copy(
                    multipartFile.getInputStream(),
                    rootLocation.resolve("%s".formatted(file.getId())),
                    StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException().setMessage("Failed to save file, " + e.getMessage());
        }

        return fileRepository.save(file);
    }

    private void saveWebpImageWithQuality(BufferedImage image, java.io.File outputFile, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();

        try (FileImageOutputStream output = new FileImageOutputStream(outputFile)) {
            writer.setOutput(output);

            // Настраиваем параметры сохранения
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            String[] compressionTypes = writeParam.getCompressionTypes();
            if (compressionTypes != null && compressionTypes.length > 0) {
                writeParam.setCompressionType(compressionTypes[0]); // Обычно это "Lossy" для WebP
            }

            writeParam.setCompressionQuality(quality); // Значение от 0 (низкое качество) до 1 (высокое качество)

            // Записываем изображение с заданным качеством
            writer.write(null, new javax.imageio.IIOImage(image, null, null), writeParam);
        } finally {
            writer.dispose();
        }
    }

    public void deleteFileForCurrentUser(UUID fileId) throws ApiException {
        var user = securityContext.getCurrentUser();

        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new ApiException().setMessage("File not found"));

        if (!file.getUserId().equals(user.getId())) {
            throw new ForbiddenException().setMessage("You don't have permission to delete this file");
        }

        Path rootLocation = Paths.get("cdn/files");
        Path filePath = rootLocation.resolve("%s".formatted(file.getId()));

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException().setMessage("Failed to delete file, " + e.getMessage());
        }

        fileRepository.delete(file);
    }

    public void deleteFilesForCurrentUser(List<UUID> fileIds) throws ApiException {
        var user = securityContext.getCurrentUser();

        var files = new ArrayList<File>();
        for (UUID fileId : fileIds) {
            File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ApiException().setMessage("File not found"));

            if (!file.getUserId().equals(user.getId())) {
                throw new ForbiddenException().setMessage("You don't have permission to delete this file");
            }

            Path rootLocation = Paths.get("cdn/files");
            Path filePath = rootLocation.resolve("%s".formatted(file.getId()));

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ApiException().setMessage("Failed to delete file, " + e.getMessage());
            }

            files.add(file);
        }
        fileRepository.deleteAll(files);
    }

    public void deleteFile(UUID fileId) throws ApiException {
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new ApiException().setMessage("File not found"));

        Path rootLocation = Paths.get("cdn/files");
        Path filePath = rootLocation.resolve("%s".formatted(file.getId()));

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException().setMessage("Failed to delete file, " + e.getMessage());
        }

        fileRepository.delete(file);
    }

    public void deleteAllUserFiles(User user) throws ApiException {
        List<File> userFiles = fileRepository.findByUserId(user.getId());

        if (userFiles.isEmpty()) {
            return;
        }

        Path rootLocation = Paths.get("cdn/files");

        for (File file : userFiles) {
            Path filePath = rootLocation.resolve("%s".formatted(file.getId()));
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ApiException().setMessage("Failed to delete file %s, %s".formatted(file.getId(), e.getMessage()));
            }
        }

        fileRepository.deleteAll(userFiles);
    }

    private String getFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null) {
            return "";
        }
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf).replace(".", "");
    }

    private boolean fileNeedToConvertInWebp(String extension) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "bmp", "tiff"};
        return Arrays.asList(imageExtensions).contains(extension.toLowerCase());
    }
}
