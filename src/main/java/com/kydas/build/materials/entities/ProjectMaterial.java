package com.kydas.build.materials.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.projects.entities.Project;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "project_materials")
@Getter
@Setter
public class ProjectMaterial extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToOne(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private Waybill waybill;

    @OneToOne(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private PassportQuality passportQuality;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
