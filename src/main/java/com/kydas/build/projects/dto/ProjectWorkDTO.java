package com.kydas.build.projects.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.projects.dto.components.ProjectWorkStageDTO;
import com.kydas.build.projects.dto.components.ProjectWorkVersionDTO;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProjectWorkVersionDTO workVersion;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProjectWorkVersionDTO> workVersions = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer completionPercent;
    private Double plannedVolume;
    private Double actualVolume;
    private String volumeUnit;
    private List<ProjectWorkStageDTO> stages = new ArrayList<>();
}

