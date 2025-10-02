package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.utils.DateUtils;
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
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate takenAt;
}
