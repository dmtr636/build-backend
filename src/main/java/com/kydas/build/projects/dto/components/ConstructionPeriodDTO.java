package com.kydas.build.projects.dto.components;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConstructionPeriodDTO {
    private LocalDate start;
    private LocalDate end;
}
