package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConstructionPeriodDTO {
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate start;
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate end;
}
