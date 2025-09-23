package com.kydas.build.projects.entities.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class ConstructionPeriod {
    private LocalDate start;
    private LocalDate end;
}
