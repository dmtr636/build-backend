package com.kydas.build.projects.entities.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
public class WorkVolume {
    @Column(name = "planned_volume")
    private BigDecimal planned;

    @Column(name = "actual_volume")
    private BigDecimal actual;

    @Column(name = "volume_unit")
    private String unit;
}
