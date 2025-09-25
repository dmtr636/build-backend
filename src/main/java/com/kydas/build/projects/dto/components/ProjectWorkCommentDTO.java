package com.kydas.build.projects.dto.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kydas.build.core.serialization.InstantToStringSerializer;
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
    @JsonSerialize(using = InstantToStringSerializer.class)
    private Instant createdAt;
    private List<UUID> fileIds;
}

