package com.kydas.build.checklists.entities;

import com.kydas.build.checklists.enums.AnswerStatus;
import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "checklist_item_answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"instance_id", "template_item_id"}))
@Getter
@Setter
public class ChecklistItemAnswer extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "instance_id")
    private ChecklistInstance instance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "template_item_id")
    private TemplateItem templateItem;

    @Enumerated(EnumType.STRING)
    private AnswerStatus answer;
}
