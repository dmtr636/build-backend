package com.kydas.build.checklists.entities;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "template_sections")
@Getter
@Setter
public class TemplateSection extends BaseEntity {
    @ManyToOne(optional = false)
    private ChecklistTemplate template;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int orderIndex;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<TemplateItem> items = new ArrayList<>();
}
