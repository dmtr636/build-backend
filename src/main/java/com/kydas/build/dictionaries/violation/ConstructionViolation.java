package com.kydas.build.dictionaries.violation;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "construction_violations")
public class ConstructionViolation extends BaseEntity {
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String kind;

    @Column(nullable = false)
    private String severityType;

    @Column(nullable = false)
    private String name;

    private Integer remediationDueDays;
}

