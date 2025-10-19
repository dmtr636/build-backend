package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.dictionaries.documents.NormativeDocumentService;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.files.File;
import com.kydas.build.files.FileDTO;
import com.kydas.build.files.FileRepository;
import com.kydas.build.notifications.NotificationService;
import com.kydas.build.notifications.NotificationType;
import com.kydas.build.projects.dto.ProjectViolationDTO;
import com.kydas.build.projects.dto.enums.ProjectViolationStatus;
import com.kydas.build.projects.entities.ProjectViolation;
import com.kydas.build.projects.mappers.ProjectViolationMapper;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.repositories.ProjectViolationRepository;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectViolationService extends BaseService<ProjectViolation, ProjectViolationDTO> {

    private final ProjectViolationRepository violationRepository;
    private final ProjectRepository projectRepository;
    private final ProjectWorkRepository workRepository;
    private final ProjectViolationMapper violationMapper;
    private final ProjectVisitService projectVisitService;
    private final NormativeDocumentService documentService;
    private final EventPublisher eventPublisher;
    private final SecurityContext securityContext;
    private final FileRepository fileRepository;
    private final NotificationService notificationService;

    public ProjectViolationService(ProjectViolationRepository violationRepository,
                                   ProjectRepository projectRepository, ProjectWorkRepository workRepository,
                                   ProjectViolationMapper violationMapper,
                                   ProjectVisitService projectVisitService,
                                   NormativeDocumentService documentService,
                                   EventPublisher eventPublisher,
                                   SecurityContext securityContext,
                                   FileRepository fileRepository,
                                   NotificationService notificationService) {
        super(ProjectViolation.class);
        this.violationRepository = violationRepository;
        this.projectRepository = projectRepository;
        this.workRepository = workRepository;
        this.violationMapper = violationMapper;
        this.projectVisitService = projectVisitService;
        this.documentService = documentService;
        this.eventPublisher = eventPublisher;
        this.securityContext = securityContext;
        this.fileRepository = fileRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ProjectViolation makeEntity(ProjectViolationDTO dto) throws ApiException {
        var violation = violationMapper.update(new ProjectViolation(), dto);
        violation.setStatus(ProjectViolationStatus.TODO);
        violation.setFiles(getFiles(dto.getFiles()));
        violation.setPhotos(getFiles(dto.getPhotos()));
        violation.setResolutionPhotos(getFiles(dto.getResolutionPhotos()));
        violation.setNormativeDocuments(documentService.getNormativeDocuments(dto.getNormativeDocuments()));
        violation.setAuthor(securityContext.getCurrentUser());
        return violation;
    }

    @Transactional
    @Override
    public ProjectViolation create(ProjectViolationDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getProjectId());
        var violation = makeEntity(dto);
        violation.setProject(project);
        if (dto.getWorkId() != null) {
            violation.setWork(workRepository.findByIdOrElseThrow(dto.getWorkId()));
        }
        var saved = violationRepository.save(violation);
        if (dto.getVisitId() != null) {
            projectVisitService.addViolationToVisit(dto.getVisitId(), saved);
        }
        notificationService.create(
                saved.getProject(),
                saved.getIsNote() ? NotificationType.ADMONITION : NotificationType.VIOLATION,
                saved.getId(),
                saved.getName(),
                saved.getAuthor()
        );
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
        violation.setResolutionPhotos(getFiles(dto.getResolutionPhotos()));
        violation.setNormativeDocuments(documentService.getNormativeDocuments(dto.getNormativeDocuments()));
        if (dto.getWorkId() != null) {
            violation.setWork(workRepository.findByIdOrElseThrow(dto.getWorkId()));
        } else {
            violation.setWork(null);
        }
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
    public ProjectViolationDTO changeStatus(UUID violationId, UUID visitId, ProjectViolationStatus newStatus) throws ApiException {
        var violation = violationRepository.findByIdOrElseThrow(violationId);
        var oldStatus = violation.getStatus();

        if (oldStatus == ProjectViolationStatus.TODO && newStatus == ProjectViolationStatus.IN_PROGRESS) {
            violation.setAssignee(securityContext.getCurrentUser());
        } else if (oldStatus == ProjectViolationStatus.IN_REVIEW && newStatus == ProjectViolationStatus.TODO) {
            violation.setAssignee(null);
        }

        violation.setStatus(newStatus);
        violation.setUpdatedAt(Instant.now());

        if (visitId != null) {
            projectVisitService.addViolationToVisit(visitId, violation);
        }

        var updated = violationRepository.save(violation);
        notificationService.create(
                updated.getProject(),
                NotificationType.VIOLATION_STATUS_UPDATE,
                updated.getId(),
                updated.getName()
        );
        publish(updated, EventWebSocketDTO.Type.UPDATE);
        return violationMapper.toDTO(updated);
    }

    private void publish(ProjectViolation violation, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-violation",
                type,
                ActionType.WORK,
                violationMapper.toDTO(violation),
                Map.of("name", violation.getName(), "projectId", violation.getProject().getId())
        );
    }

    private List<File> getFiles(List<FileDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }
        var ids = dtos.stream()
                .map(FileDTO::getId)
                .toList();
        return fileRepository.findAllById(ids);
    }
}
