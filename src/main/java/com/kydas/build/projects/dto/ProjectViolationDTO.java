package com.kydas.build.projects.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import com.kydas.build.files.FileDTO;
import com.kydas.build.projects.dto.enums.ProjectViolationStatus;
import com.kydas.build.users.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProjectViolationDTO extends BaseDTO {
    private UUID projectId;
    private String name;
    private LocalDate dueDate;
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant violationTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProjectViolationStatus status;
    private String category;
    private String kind;
    private String severityType;
    private Boolean isNote = false;
    private Double latitude;
    private Double longitude;
    private List<FileDTO> files = new ArrayList<>();
    private List<FileDTO> photos = new ArrayList<>();
    private List<FileDTO> resolutionPhotos = new ArrayList<>();
    private List<ProjectViolationCommentDTO> comments = new ArrayList<>();
    private UserDTO author;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO assignee;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID visitId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant updatedAt;
}
