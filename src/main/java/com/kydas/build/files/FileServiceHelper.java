package com.kydas.build.files;

import com.kydas.build.core.exceptions.classes.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FileServiceHelper {

    private final FileMapper fileMapper;
    private final FileRepository fileRepository;

    public List<File> mapFiles(List<FileDTO> filesDto) {
        if (filesDto == null) return Collections.emptyList();
        return filesDto.stream()
                .map(dto -> fileMapper.update(new File(), dto))
                .collect(Collectors.toList());
    }

    public List<File> fetchFiles(List<FileDTO> filesDto) throws ApiException {
        if (filesDto == null) return Collections.emptyList();

        List<File> files = new ArrayList<>();
        for (FileDTO dto : filesDto) {
            File file = fileRepository.findById(dto.getId())
                    .orElseThrow(() -> new ApiException().setMessage("File not found: " + dto.getId()));
            files.add(file);
        }
        return files;
    }
}
