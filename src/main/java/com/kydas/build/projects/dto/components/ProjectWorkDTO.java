package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProjectWorkDTO {
    private UUID id;
    private UUID projectId;
    private String name;
    private String status;
    private ConstructionPeriodDTO plannedPeriod;
    private ConstructionPeriodDTO actualPeriod;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer completionPercent;
    private Double plannedVolume;
    private Double actualVolume;
    private String volumeUnit;
    private List<ProjectWorkStageDTO> stages = new ArrayList<>();
}

