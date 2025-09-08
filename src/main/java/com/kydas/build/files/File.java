package com.kydas.build.files;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(indexes = @Index(columnList = "userId"))
public class File {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private Long size;

    private UUID userId;

    private String type;

    @CreationTimestamp
    private OffsetDateTime createDate;

    public enum Type {
        PROFILE_IMAGE, PROJECT_COVER_IMAGE, PROJECT_CONTENT_IMAGE, PROJECT_CONTENT_VIDEO, REPORT_ATTACHMENT_IMAGE, REVIEW_IMAGE
    }
}
