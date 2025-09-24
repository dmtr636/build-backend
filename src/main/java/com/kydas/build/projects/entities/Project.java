package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.organizations.Organization;
import com.kydas.build.projects.entities.embeddable.Address;
import com.kydas.build.projects.entities.embeddable.ConstructionPeriod;
import com.kydas.build.projects.entities.embeddable.Coordinate;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String objectNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "house", column = @Column(name = "address_house"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "centroid_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "centroid_longitude"))
    })
    private Coordinate centroid;

    @ElementCollection
    @CollectionTable(
            name = "project_coordinates",
            joinColumns = @JoinColumn(name = "project_id")
    )
    private List<Coordinate> polygon;

    @ManyToOne
    @JoinColumn(name = "customer_organization_id")
    private Organization customerOrganization;

    @ManyToOne
    @JoinColumn(name = "contractor_organization_id")
    private Organization contractorOrganization;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectUser> projectUsers = new ArrayList<>();

    private Instant lastInspection;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "planned_start")),
            @AttributeOverride(name = "end", column = @Column(name = "planned_end"))
    })
    private ConstructionPeriod plannedPeriod;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "actual_start")),
            @AttributeOverride(name = "end", column = @Column(name = "actual_end"))
    })
    private ConstructionPeriod actualPeriod;

    private String type;

    private String imageId;

    private String status;

    private Boolean hasViolations;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectImage> gallery = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectWork> works = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
