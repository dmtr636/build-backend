package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.files.File;
import com.kydas.build.projects.dto.enums.ProjectViolationStatus;
import com.kydas.build.projects.entities.embeddable.Coordinate;
import com.kydas.build.users.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project_violations")
public class ProjectViolation extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "violation_time", nullable = false)
    private Instant violationTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectViolationStatus status;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String kind;

    @Column(name = "severity_type", nullable = false)
    private String severityType;

    @Column(name = "is_note", nullable = false)
    private Boolean isNote = false;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Coordinate coordinate;

    @OneToMany
    @JoinTable(
            name = "project_violation_files",
            joinColumns = @JoinColumn(name = "violation_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "project_violation_photos",
            joinColumns = @JoinColumn(name = "violation_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> photos = new ArrayList<>();

    @OneToMany(mappedBy = "violation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectViolationComment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}

