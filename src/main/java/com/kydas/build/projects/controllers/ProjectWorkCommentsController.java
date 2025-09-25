package com.kydas.build.projects.controllers;


import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.projects.dto.ProjectWorkCommentDTO;
import com.kydas.build.projects.entities.ProjectWorkComment;
import com.kydas.build.projects.services.ProjectWorkCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_WORKS_COMMENTS)
@Tag(name = "Сервис состава работ на объекте")
public class ProjectWorkCommentsController extends BaseController<ProjectWorkComment, ProjectWorkCommentDTO> {

    private final ProjectWorkCommentService service;

    public ProjectWorkCommentsController(ProjectWorkCommentService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @Operation(summary = "Получение всех комментариев к работам на объекте")
    public List<ProjectWorkCommentDTO> search(@RequestParam(required = false) UUID workId) {
        return service.getByWorkId(workId);
    }
}

