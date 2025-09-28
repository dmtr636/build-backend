package com.kydas.build.checklists.entities;

import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.projects.entities.Project;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_instances",
        indexes = {
                @Index(name = "ix_ci_project_type_date", columnList = "project_id,type,checkDate")
        })
@Getter
@Setter
public class ChecklistInstance extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChecklistFormType type;

    @ManyToOne(optional = false)
    private ChecklistTemplate template;

    private LocalDate checkDate;

    private String status;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItemAnswer> answers = new ArrayList<>();
}
