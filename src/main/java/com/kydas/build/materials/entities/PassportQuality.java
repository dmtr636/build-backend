package com.kydas.build.materials.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.files.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passport_qualities")
@Getter
@Setter
public class PassportQuality extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private ProjectMaterial material;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String consumerNameAndAddress;

    private String contractNumber;

    @Column(nullable = false)
    private String productNameAndGrade;

    private String batchNumber;

    private Integer batchCount;

    private LocalDate manufactureDate;

    private Integer shippedQuantity;

    private String labChief;

    @OneToMany
    @JoinTable(
            name = "passport_quality_files",
            joinColumns = @JoinColumn(name = "passport_quality_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "passport_quality_images",
            joinColumns = @JoinColumn(name = "passport_quality_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> images = new ArrayList<>();
}
