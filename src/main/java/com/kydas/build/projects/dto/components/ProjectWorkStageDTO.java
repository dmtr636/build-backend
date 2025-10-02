package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.utils.DateUtils;
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
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate date;
}

