package com.kydas.build.projects.dto.components;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectUserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private String side;
    private Boolean isResponsible;
}