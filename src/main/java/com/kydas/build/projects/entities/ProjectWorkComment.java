package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.users.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "project_work_comments")
public class ProjectWorkComment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private ProjectWork work;

    @Column(nullable = false, length = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ElementCollection
    @CollectionTable(
            name = "project_work_comment_files",
            joinColumns = @JoinColumn(name = "comment_id")
    )
    @Column(name = "file_id")
    private List<UUID> fileIds = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
