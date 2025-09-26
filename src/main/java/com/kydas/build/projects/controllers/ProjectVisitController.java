package com.kydas.build.projects.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.projects.dto.ProjectVisitDTO;
import com.kydas.build.projects.entities.ProjectVisit;
import com.kydas.build.projects.services.ProjectVisitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(Endpoints.PROJECTS_VISITS)
@Tag(name = "Сервис визитов объекта")
public class ProjectVisitController extends BaseController<ProjectVisit, ProjectVisitDTO> {

    private final ProjectVisitService service;

    public ProjectVisitController(ProjectVisitService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public List<ProjectVisitDTO> getByProject(@RequestParam UUID projectId) {
        return service.getByProject(projectId);
    }

    @GetMapping("/lookup")
    public ProjectVisitDTO findByProjectUserAndDate(
            @RequestParam UUID projectId,
            @RequestParam UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws ApiException {
        return service.getByProjectUserAndDate(projectId, userId, date);
    }
}
