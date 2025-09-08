package com.kydas.build.files;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.response.OkResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kydas.build.core.endpoints.Endpoints.FILES_ENDPOINT;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class FileController {
    private final FileService fileService;

    @GetMapping(FILES_ENDPOINT)
    public List<FileDTO> getAll() throws ApiException {
        var entities = fileService.getAllCurrentUserFiles();
        return entities.stream().map(FileDTO::new).collect(Collectors.toList());
    }

    @PostMapping(FILES_ENDPOINT + "/upload")
    public FileDTO upload(@RequestParam MultipartFile file, @RequestParam File.Type type) throws ApiException {
        File uploadedFile = fileService.uploadFileForCurrentUser(file, type);
        return new FileDTO(uploadedFile);
    }

    @DeleteMapping(FILES_ENDPOINT + "/{id}")
    public OkResponse delete(@PathVariable UUID id) throws ApiException {
        fileService.deleteFileForCurrentUser(id);
        return new OkResponse();
    }

    @PostMapping(FILES_ENDPOINT + "/delete")
    public OkResponse deleteByIds(@RequestBody @Valid DeleteFilesDTO dto) throws ApiException {
        fileService.deleteFilesForCurrentUser(dto.getFileIds());
        return new OkResponse();
    }

    @Data
    public static class DeleteFilesDTO {
        @NotNull
        private List<UUID> fileIds;
    }
}
