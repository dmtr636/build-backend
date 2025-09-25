package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "project_work_stages")
public class ProjectWorkStage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private ProjectWork work;

    @Column(nullable = false)
    private String name;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    private String status;

    private LocalDate date;
}

