package com.kydas.build.projects.dto.components;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectDocumentDTO {
    private UUID id;
    private UUID fileId;
    private String documentGroup;
}
