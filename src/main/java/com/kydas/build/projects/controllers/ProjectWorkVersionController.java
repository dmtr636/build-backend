package com.kydas.build.projects.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.projects.dto.components.ProjectWorkVersionDTO;
import com.kydas.build.projects.entities.ProjectWorkVersion;
import com.kydas.build.projects.services.ProjectWorkVersionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoints.PROJECTS_WORK_VERSIONS)
@Tag(name = "Сервис версий работ объекта")
public class ProjectWorkVersionController extends BaseController<ProjectWorkVersion, ProjectWorkVersionDTO> {

    private final ProjectWorkVersionService service;

    @GetMapping("/search")
    public List<ProjectWorkVersionDTO> getVersionsByWork(@RequestParam UUID workId) {
        return service.getByWorkId(workId);
    }
}
