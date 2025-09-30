package com.kydas.build.materials.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.materials.dtos.ProjectMaterialDTO;
import com.kydas.build.materials.entities.ProjectMaterial;
import com.kydas.build.materials.servicies.ProjectMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_MATERIALS)
@Tag(name = "Сервис материалов объекта")
@RequiredArgsConstructor
public class ProjectMaterialController extends BaseController<ProjectMaterial, ProjectMaterialDTO> {

    private final ProjectMaterialService service;

    @GetMapping("/search")
    public List<ProjectMaterialDTO> getByProject(@RequestParam UUID projectId) {
        return service.getByProject(projectId);
    }
}
