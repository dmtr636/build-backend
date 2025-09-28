package com.kydas.build.checklists.entities;

import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_templates", uniqueConstraints = @UniqueConstraint(columnNames = {"type"}))
@Getter
@Setter
public class ChecklistTemplate extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChecklistFormType type;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<TemplateSection> sections = new ArrayList<>();
}
