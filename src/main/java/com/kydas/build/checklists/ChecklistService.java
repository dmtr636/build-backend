package com.kydas.build.checklists;

import com.kydas.build.checklists.dto.ChecklistInstanceDTO;
import com.kydas.build.checklists.dto.ChecklistItemAnswerDTO;
import com.kydas.build.checklists.dto.ChecklistSectionDTO;
import com.kydas.build.checklists.dto.ChecklistSubmitDTO;
import com.kydas.build.checklists.entities.ChecklistInstance;
import com.kydas.build.checklists.entities.ChecklistItemAnswer;
import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.checklists.mappers.ChecklistInstanceMapper;
import com.kydas.build.checklists.mappers.ChecklistItemAnswerMapper;
import com.kydas.build.checklists.mappers.TemplateSectionMapper;
import com.kydas.build.checklists.repositories.ChecklistInstanceRepository;
import com.kydas.build.checklists.repositories.ChecklistTemplateRepository;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.projects.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ProjectRepository projectRepository;
    private final ChecklistInstanceRepository checklistInstanceRepository;
    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final ChecklistInstanceMapper checklistInstanceMapper;
    private final TemplateSectionMapper sectionMapper;
    private final ChecklistItemAnswerMapper answerMapper;
    private final EventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ChecklistInstanceDTO> getChecklistsByType(UUID projectId, ChecklistFormType type) throws ApiException {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException().setMessage("Project not found"));

        List<ChecklistInstance> instances;
        if (type == ChecklistFormType.OPENING) {
            var opening = project.getOpeningChecklist();
            instances = opening == null ? List.of() : List.of(opening);
        } else {
            instances = project.getDailyChecklists().stream()
                    .filter(c -> c.getType() == type)
                    .sorted(Comparator.comparing(ChecklistInstance::getCheckDate).reversed())
                    .toList();
        }

        return instances.stream()
                .map(this::buildChecklistInstanceDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChecklistSectionDTO> getChecklistTemplateByType(ChecklistFormType type) throws ApiException {
        var template = checklistTemplateRepository.findByType(type)
                .orElseThrow(() -> new NotFoundException().setMessage("Checklist template not found"));

        return template.getSections().stream()
                .map(section -> {
                    var sectionDTO = sectionMapper.toDTO(section);
                    var items = section.getItems().stream()
                            .map(item -> {
                                var dto = new ChecklistItemAnswerDTO();
                                dto.setTemplateItemId(item.getId());
                                dto.setItemNumber(item.getItemNumber());
                                dto.setText(item.getText());
                                dto.setAnswer(null);
                                return dto;
                            })
                            .toList();
                    sectionDTO.setItems(items);
                    return sectionDTO;
                })
                .toList();
    }

    @Transactional
    public ChecklistInstanceDTO createChecklistInstance(UUID projectId, ChecklistFormType type, List<ChecklistItemAnswerDTO> answers) throws ApiException {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException().setMessage("Project not found"));

        var template = checklistTemplateRepository.findByType(type)
                .orElseThrow(() -> new NotFoundException().setMessage("Checklist template not found"));

        var instance = new ChecklistInstance();
        instance.setProject(project);
        instance.setTemplate(template);
        instance.setType(type);
        instance.setStatus("IN_PROGRESS");
        instance.setCheckDate(LocalDate.now());

        for (var section : template.getSections()) {
            for (var item : section.getItems()) {
                var answer = new ChecklistItemAnswer();
                answer.setInstance(instance);
                answer.setTemplateItem(item);

                var dto = answers.stream()
                        .filter(a -> a.getTemplateItemId().equals(item.getId()))
                        .findFirst()
                        .orElse(null);

                answer.setAnswer(dto != null ? dto.getAnswer() : null);
                instance.getAnswers().add(answer);
            }
        }

        if (type == ChecklistFormType.OPENING) {
            project.setOpeningChecklist(instance);
        } else {
            project.getDailyChecklists().add(instance);
        }

        checklistInstanceRepository.save(instance);
        checklistInstanceRepository.flush();

        publish(instance, EventWebSocketDTO.Type.CREATE);

        return buildChecklistInstanceDTO(instance);
    }

    @Transactional
    public ChecklistInstanceDTO submitAnswers(UUID projectId, ChecklistSubmitDTO submitDTO) throws ApiException {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException().setMessage("Project not found"));

        var instance = checklistInstanceRepository.findById(submitDTO.getChecklistInstanceId())
                .orElseThrow(() -> new NotFoundException().setMessage("Checklist instance not found"));

        for (var answerDTO : submitDTO.getAnswers()) {
            var answer = instance.getAnswers().stream()
                    .filter(a -> a.getTemplateItem().getId().equals(answerDTO.getTemplateItemId()))
                    .findFirst()
                    .orElseGet(() -> {
                        var newAnswer = new ChecklistItemAnswer();
                        newAnswer.setInstance(instance);
                        newAnswer.setTemplateItem(
                                instance.getTemplate().getSections().stream()
                                        .flatMap(s -> s.getItems().stream())
                                        .filter(i -> i.getId().equals(answerDTO.getTemplateItemId()))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("TemplateItem not found"))
                        );
                        instance.getAnswers().add(newAnswer);
                        return newAnswer;
                    });

            answer.setAnswer(answerDTO.getAnswer());
        }
        instance.setStatus(submitDTO.getStatus());
        checklistInstanceRepository.save(instance);

        publish(instance, EventWebSocketDTO.Type.UPDATE);

        return buildChecklistInstanceDTO(instance);
    }

    private void publish(ChecklistInstance instance, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "checklist",
                type,
                ActionType.WORK,
                checklistInstanceMapper.toDTO(instance),
                Map.of("checklistInstanceId", instance.getId(), "projectId", instance.getProject().getId())
        );
    }

    private ChecklistInstanceDTO buildChecklistInstanceDTO(ChecklistInstance instance) {
        var dto = checklistInstanceMapper.toDTO(instance);

        var sections = instance.getTemplate().getSections().stream()
                .map(section -> {
                    var sectionDTO = sectionMapper.toDTO(section);
                    var items = section.getItems().stream()
                            .map(item -> instance.getAnswers().stream()
                                    .filter(a -> a.getTemplateItem().getId().equals(item.getId()))
                                    .findFirst()
                                    .map(answerMapper::toDTOWithTemplate)
                                    .orElseGet(() -> {
                                        var newItem = new ChecklistItemAnswerDTO();
                                        newItem.setTemplateItemId(item.getId());
                                        newItem.setItemNumber(item.getItemNumber());
                                        newItem.setText(item.getText());
                                        newItem.setAnswer(null);
                                        return newItem;
                                    }))
                            .toList();
                    sectionDTO.setItems(items);
                    return sectionDTO;
                })
                .toList();

        dto.setSections(sections);
        return dto;
    }
}
