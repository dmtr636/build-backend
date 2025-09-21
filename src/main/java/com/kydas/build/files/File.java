package com.kydas.build.files;

import jakarta.persistence.*;
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
public class File {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID id;

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

    @Version
    @Column(nullable = false)
    private int version;

    public String getEntityName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "%s { id = %s }".formatted(getEntityName(), id);
    }
}
