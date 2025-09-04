package com.kydas.build.users;

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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "users_idx_login", columnList = "login", unique = true),
        @Index(name = "users_idx_role", columnList = "role"),
        @Index(name = "users_idx_position", columnList = "position"),
        @Index(name = "users_idx_name", columnList = "name"),
        @Index(name = "users_idx_enabled", columnList = "enabled"),
        @Index(name = "users_idx_createDate", columnList = "createDate"),
        @Index(name = "users_idx_updateDate", columnList = "updateDate"),
    }
)
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean enabled;

    @Nullable
    private String position;

    @Nullable
    private String name;

    @Nullable
    private String imageId;

    @CreationTimestamp
    private Instant createDate;

    @UpdateTimestamp
    private Instant updateDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> info;

    public enum Role {
        ROOT, ADMIN, USER
    }
}
