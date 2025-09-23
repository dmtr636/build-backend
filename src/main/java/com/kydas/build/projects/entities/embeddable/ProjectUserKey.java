package com.kydas.build.projects.entities.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class ProjectUserKey implements Serializable {
    private UUID projectId;
    private UUID userId;
}
