package com.kydas.build.projects.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.projects.dto.components.ProjectWorkDTO;
import com.kydas.build.projects.entities.ProjectWork;
import com.kydas.build.projects.services.ProjectWorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_WORKS)
@Tag(name = "Сервис работ объекта")
public class ProjectWorkController extends BaseController<ProjectWork, ProjectWorkDTO> {

    private final ProjectWorkService service;

    public ProjectWorkController(ProjectWorkService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @Operation(summary = "Получение всех работ по объекту")
    public List<ProjectWorkDTO> search(@RequestParam(required = false) UUID projectId) {
        return service.getByProjectId(projectId);
    }
}
