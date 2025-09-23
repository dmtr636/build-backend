package com.kydas.build.projects.dto.components;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ProjectImageDTO {
    private UUID id;
    private UUID fileId;
    private String caption;
    private LocalDate takenAt;
}
