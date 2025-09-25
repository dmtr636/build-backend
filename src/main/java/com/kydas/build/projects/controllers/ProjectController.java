package com.kydas.build.projects.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.projects.dto.ProjectDTO;
import com.kydas.build.projects.entities.Project;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.PROJECTS)
@Tag(name = "Сервис строительных объектов")
public class ProjectController extends BaseController<Project, ProjectDTO> {
}
