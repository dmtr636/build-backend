package com.kydas.build.projects.controllers;


import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.projects.dto.ProjectViolationCommentDTO;
import com.kydas.build.projects.entities.ProjectViolationComment;
import com.kydas.build.projects.services.ProjectViolationCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_VIOLATIONS_COMMENTS)
@Tag(name = "Сервис нарушений на объекте")
public class ProjectViolationCommentsController extends BaseController<ProjectViolationComment, ProjectViolationCommentDTO> {

    private final ProjectViolationCommentService service;

    public ProjectViolationCommentsController(ProjectViolationCommentService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @Operation(summary = "Получение всех комментариев к нарушениям на объекте")
    public List<ProjectViolationCommentDTO> search(@RequestParam(required = false) UUID violationId) {
        return service.getByViolationId(violationId);
    }
}

