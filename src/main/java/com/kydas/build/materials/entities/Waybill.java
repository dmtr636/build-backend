package com.kydas.build.materials.entities;

import com.kydas.build.core.crud.BaseEntity;
import com.kydas.build.files.File;
import com.kydas.build.projects.entities.ProjectWork;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waybills")
@Getter
@Setter
public class Waybill extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private ProjectMaterial material;

    private String materialName;

    private String receiver;

    private Instant deliveryDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_work_id")
    private ProjectWork projectWork;

    private String invoiceNumber;

    private String volume;

    private String netWeight;

    private String grossWeight;

    private Integer packageCount;

    @Column(name = "laboratory_analysis_required", nullable = false)
    private Boolean laboratoryAnalysisRequired = false;

    @OneToMany
    @JoinTable(
            name = "waybill_files",
            joinColumns = @JoinColumn(name = "waybill_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "waybill_images",
            joinColumns = @JoinColumn(name = "waybill_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> images = new ArrayList<>();
}
