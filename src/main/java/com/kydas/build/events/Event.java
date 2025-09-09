package com.kydas.build.events;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "events",
    indexes = {
        @Index(name = "events_idx_userId", columnList = "userId"),
        @Index(name = "events_idx_action", columnList = "action"),
        @Index(name = "events_idx_actionType", columnList = "actionType"),
        @Index(name = "events_idx_objectName", columnList = "objectName"),
        @Index(name = "events_idx_objectId", columnList = "objectId"),
        @Index(name = "events_idx_date", columnList = "date"),
    }
)
public class Event extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String actionType;

    @Nullable
    private String objectName;

    @Nullable
    private String objectId;

    @CreationTimestamp
    private Instant date;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> info;
}
