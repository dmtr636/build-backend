package com.kydas.build.projects.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.projects.dto.ProjectViolationDTO;
import com.kydas.build.projects.dto.enums.ProjectViolationStatus;
import com.kydas.build.projects.entities.ProjectViolation;
import com.kydas.build.projects.services.ProjectViolationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_VIOLATIONS)
@Tag(name = "Сервис нарушений объекта")
public class ProjectViolationController extends BaseController<ProjectViolation, ProjectViolationDTO> {

    private final ProjectViolationService service;

    public ProjectViolationController(ProjectViolationService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @Operation(summary = "Получение всех нарушений по объекту")
    public List<ProjectViolationDTO> search(@RequestParam(required = false) UUID projectId) {
        return service.getByProjectId(projectId);
    }

    @PatchMapping("/{id}/status")
    public ProjectViolationDTO changeStatus(@PathVariable UUID id,
                                            @RequestParam ProjectViolationStatus status) throws ApiException {
        return service.changeStatus(id, status);
    }
}
