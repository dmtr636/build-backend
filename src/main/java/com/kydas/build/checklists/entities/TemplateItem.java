package com.kydas.build.checklists.entities;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "template_items",
        indexes = @Index(name = "ix_template_item_number", columnList = "itemNumber"))
@Getter
@Setter
public class TemplateItem extends BaseEntity {
    @ManyToOne(optional = false)
    private TemplateSection section;

    @Column(nullable = false)
    private String itemNumber;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int orderIndex;

    @Column(nullable = false)
    private boolean required;
}
