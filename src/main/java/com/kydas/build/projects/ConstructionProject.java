package com.kydas.build.projects;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.organizations.Organization;
import com.kydas.build.users.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "construction_projects")
public class ConstructionProject extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private Double latitude; // широта

    @Column(nullable = false)
    private Double longitude; // долгота

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_user_id")
    private User responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_organization_id")
    private Organization customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_organization_id")
    private Organization contractor;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Nullable
    private String imageId;

    @CreationTimestamp
    private Instant createdAt;
}
