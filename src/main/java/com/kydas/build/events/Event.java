package com.kydas.build.events;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "events")
public class Event extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String actionType;

    private String objectName;

    private String objectId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> info;
}
