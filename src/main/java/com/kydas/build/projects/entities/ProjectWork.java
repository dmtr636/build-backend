package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.projects.entities.embeddable.WorkVolume;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project_works")
public class ProjectWork extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("versionNumber DESC")
    private List<ProjectWorkVersion> workVersions = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "planned", column = @Column(name = "planned_volume")),
            @AttributeOverride(name = "actual", column = @Column(name = "actual_volume")),
            @AttributeOverride(name = "unit", column = @Column(name = "volume_unit"))
    })
    private WorkVolume volume;

    @Column(nullable = false)
    private Integer completionPercent = 0;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNumber ASC")
    private List<ProjectWorkStage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<ProjectWorkComment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "works")
    private List<ProjectVisit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<ProjectViolation> violations = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
