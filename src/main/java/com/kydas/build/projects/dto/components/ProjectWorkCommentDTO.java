package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProjectWorkCommentDTO {
    private UUID id;
    private UUID workId;
    private String text;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID authorId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;
    private List<UUID> fileIds;
}

