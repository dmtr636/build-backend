package com.kydas.build.files;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File extends BaseEntity {
    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private Long size;

    private UUID userId;

    private String type;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public enum Type {
        PROFILE_IMAGE, PROJECT_COVER_IMAGE, PROJECT_CONTENT_IMAGE, PROJECT_CONTENT_VIDEO, REPORT_ATTACHMENT_IMAGE, REVIEW_IMAGE
    }
}
