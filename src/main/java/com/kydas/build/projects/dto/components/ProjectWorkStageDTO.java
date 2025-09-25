package com.kydas.build.projects.dto.components;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ProjectWorkStageDTO {
    private UUID id;
    private String name;
    private Integer orderNumber;
    private String status;
    private LocalDate date;
}

