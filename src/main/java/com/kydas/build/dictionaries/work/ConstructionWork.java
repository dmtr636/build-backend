package com.kydas.build.dictionaries.work;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "construction_works")
public class ConstructionWork extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String unit;

    private String classificationCode;
}
