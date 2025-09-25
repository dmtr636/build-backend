package com.kydas.build.projects.dto.components;

import com.kydas.build.files.FileDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectDocumentDTO {
    private UUID id;
    private String name;
    private FileDTO file;
    private String documentGroup;
}
