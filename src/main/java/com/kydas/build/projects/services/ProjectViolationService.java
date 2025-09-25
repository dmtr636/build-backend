package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.files.File;
import com.kydas.build.files.FileDTO;
import com.kydas.build.files.FileRepository;
import com.kydas.build.projects.dto.ProjectViolationDTO;
import com.kydas.build.projects.dto.enums.ProjectViolationStatus;
import com.kydas.build.projects.entities.ProjectViolation;
import com.kydas.build.projects.mappers.ProjectViolationMapper;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.repositories.ProjectViolationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectViolationService extends BaseService<ProjectViolation, ProjectViolationDTO> {

    private final ProjectViolationRepository violationRepository;
    private final ProjectRepository projectRepository;
    private final ProjectViolationMapper violationMapper;
    private final EventPublisher eventPublisher;
    private final SecurityContext securityContext;
    private final FileRepository fileRepository;

    public ProjectViolationService(ProjectViolationRepository violationRepository,
                                   ProjectRepository projectRepository,
                                   ProjectViolationMapper violationMapper,
                                   EventPublisher eventPublisher,
                                   SecurityContext securityContext,
                                   FileRepository fileRepository) {
        super(ProjectViolation.class);
        this.violationRepository = violationRepository;
        this.projectRepository = projectRepository;
        this.violationMapper = violationMapper;
        this.eventPublisher = eventPublisher;
        this.securityContext = securityContext;
        this.fileRepository = fileRepository;
    }

    @Override
    public ProjectViolation makeEntity(ProjectViolationDTO dto) throws ApiException {
        var violation = violationMapper.update(new ProjectViolation(), dto);
        violation.setStatus(ProjectViolationStatus.TODO);
        violation.setFiles(getFiles(dto.getFiles()));
        violation.setPhotos(getFiles(dto.getPhotos()));
        violation.setAuthor(securityContext.getCurrentUser());
        return violation;
    }

    @Transactional
    @Override
    public ProjectViolation create(ProjectViolationDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getProjectId());
        var violation = makeEntity(dto);
        violation.setProject(project);
        var saved = violationRepository.save(violation);
        publish(saved, EventWebSocketDTO.Type.CREATE);
        return saved;
    }

    @Transactional
    @Override
    public ProjectViolation update(ProjectViolationDTO dto) throws ApiException {
        var violation = violationRepository.findByIdOrElseThrow(dto.getId());
        violationMapper.update(violation, dto);
        violation.setFiles(getFiles(dto.getFiles()));
        violation.setPhotos(getFiles(dto.getPhotos()));
        var updated = violationRepository.save(violation);
        publish(updated, EventWebSocketDTO.Type.UPDATE);
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var violation = violationRepository.findByIdOrElseThrow(id);
        violationRepository.delete(violation);
        publish(violation, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectViolationDTO> getByProjectId(UUID projectId) {
        return violationRepository.findByProjectId(projectId).stream()
                .map(violationMapper::toDTO)
                .toList();
    }

    @Transactional
    public ProjectViolationDTO changeStatus(UUID violationId, ProjectViolationStatus newStatus) throws ApiException {
        var violation = violationRepository.findByIdOrElseThrow(violationId);
        var oldStatus = violation.getStatus();

        if (oldStatus == ProjectViolationStatus.TODO && newStatus == ProjectViolationStatus.IN_PROGRESS) {
            violation.setAssignee(securityContext.getCurrentUser());
        } else if (oldStatus == ProjectViolationStatus.IN_REVIEW && newStatus == ProjectViolationStatus.TODO) {
            violation.setAssignee(null);
        }

        violation.setStatus(newStatus);
        violation.setUpdatedAt(Instant.now());

        var updated = violationRepository.save(violation);
        publish(updated, EventWebSocketDTO.Type.UPDATE);

        return violationMapper.toDTO(updated);
    }

    private void publish(ProjectViolation violation, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-violation",
                type,
                ActionType.WORK,
                violationMapper.toDTO(violation),
                Map.of("name", violation.getName())
        );
    }

    private List<File> getFiles(List<FileDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        var ids = dtos.stream()
                .map(FileDTO::getId)
                .toList();
        return fileRepository.findAllById(ids);
    }
}
