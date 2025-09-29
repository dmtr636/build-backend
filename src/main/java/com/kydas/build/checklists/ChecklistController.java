package com.kydas.build.checklists;

import com.kydas.build.checklists.dto.ChecklistInstanceDTO;
import com.kydas.build.checklists.dto.ChecklistItemAnswerDTO;
import com.kydas.build.checklists.dto.ChecklistSectionDTO;
import com.kydas.build.checklists.dto.ChecklistSubmitDTO;
import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.PROJECTS_CHECKLISTS)
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService service;

    @GetMapping
    public List<ChecklistSectionDTO> getEmptyChecklist(@RequestParam ChecklistFormType type) throws ApiException {
        return service.getChecklistTemplateByType(type);
    }

    @PostMapping("/{projectId}/submit")
    public ChecklistInstanceDTO createChecklistInstance(
            @PathVariable UUID projectId,
            @RequestParam ChecklistFormType type,
            @RequestBody List<ChecklistItemAnswerDTO> answers
    ) throws ApiException {
        return service.createChecklistInstance(projectId, type, answers);
    }

    @GetMapping("/{projectId}")
    public List<ChecklistInstanceDTO> getChecklistsByProjectId(
            @PathVariable UUID projectId,
            @RequestParam ChecklistFormType type
    ) throws ApiException {
        return service.getChecklistsByType(projectId, type);
    }

    @PutMapping("/{projectId}")
    public ChecklistInstanceDTO submitAnswers(
            @PathVariable UUID projectId,
            @RequestBody ChecklistSubmitDTO submitDTO
    ) throws ApiException {
        return service.submitAnswers(projectId, submitDTO);
    }
}
