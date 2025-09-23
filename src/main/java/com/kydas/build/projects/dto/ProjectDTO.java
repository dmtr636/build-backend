package com.kydas.build.projects.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kydas.build.core.serialization.InstantFromStringDeserializer;
import com.kydas.build.core.serialization.InstantToStringSerializer;
import com.kydas.build.projects.dto.components.AddressDTO;
import com.kydas.build.projects.dto.components.ConstructionPeriodDTO;
import com.kydas.build.projects.dto.components.CoordinateDTO;
import com.kydas.build.projects.dto.components.ProjectDocumentDTO;
import com.kydas.build.projects.dto.components.ProjectImageDTO;
import com.kydas.build.projects.dto.components.ProjectUserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProjectDTO {
    private UUID id;
    private String name;
    private String objectNumber;
    private AddressDTO address;
    private CoordinateDTO centroid;
    private List<CoordinateDTO> polygon;
    private UUID customerOrganization;
    private UUID contractorOrganization;
    private List<ProjectUserDTO> projectUsers;
    @JsonSerialize(using = InstantToStringSerializer.class)
    @JsonDeserialize(using = InstantFromStringDeserializer.class)
    private Instant lastInspection;
    private ConstructionPeriodDTO plannedPeriod;
    private ConstructionPeriodDTO actualPeriod;
    private String type;
    private String imageId;
    private String status;
    private Boolean hasViolations;
    private List<ProjectImageDTO> gallery;
    private List<ProjectDocumentDTO> documents;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = InstantToStringSerializer.class)
    private Instant createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = InstantToStringSerializer.class)
    private Instant updatedAt;
}
