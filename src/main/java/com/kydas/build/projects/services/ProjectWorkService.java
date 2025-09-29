package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.notifications.NotificationService;
import com.kydas.build.notifications.NotificationType;
import com.kydas.build.projects.dto.ProjectWorkDTO;
import com.kydas.build.projects.entities.ProjectWork;
import com.kydas.build.projects.entities.ProjectWorkStage;
import com.kydas.build.projects.mappers.ProjectWorkMapper;
import com.kydas.build.projects.mappers.ProjectWorkStageMapper;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProjectWorkService extends BaseService<ProjectWork, ProjectWorkDTO> {

    private final ProjectWorkRepository workRepository;
    private final ProjectRepository projectRepository;
    private final ProjectWorkMapper workMapper;
    private final ProjectWorkStageMapper stageMapper;
    private final ProjectVisitService visitService;
    private final ProjectWorkVersionService versionService;
    private final NotificationService notificationService;
    private final EventPublisher eventPublisher;

    public ProjectWorkService(ProjectWorkRepository workRepository,
                              ProjectRepository projectRepository,
                              ProjectWorkMapper workMapper,
                              ProjectWorkStageMapper stageMapper,
                              ProjectVisitService visitService,
                              ProjectWorkVersionService versionService,
                              NotificationService notificationService,
                              EventPublisher eventPublisher) {
        super(ProjectWork.class);
        this.workRepository = workRepository;
        this.projectRepository = projectRepository;
        this.workMapper = workMapper;
        this.stageMapper = stageMapper;
        this.visitService = visitService;
        this.versionService = versionService;
        this.notificationService = notificationService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ProjectWork makeEntity(ProjectWorkDTO dto) {
        var work = workMapper.update(new ProjectWork(), dto);
        setStages(work, dto);
        updateCompletionPercent(work);
        return work;
    }

    @Transactional
    @Override
    public ProjectWork create(ProjectWorkDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getProjectId());
        var work = makeEntity(dto);
        work.setProject(project);
        var saved = saveAndPublish(work, EventWebSocketDTO.Type.CREATE);
        if (Objects.nonNull(dto.getWorkVersion())) {
            var versionDTO = dto.getWorkVersion();
            versionDTO.setWorkId(saved.getId());
            var version = versionService.create(versionDTO);
            saved.getWorkVersions().add(version);
        }
        return saved;
    }

    @Transactional
    @Override
    public ProjectWork update(ProjectWorkDTO dto) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(dto.getId());
        workMapper.update(work, dto);
        setStages(work, dto);
        updateCompletionPercent(work);
        var updated = saveAndPublish(work, EventWebSocketDTO.Type.UPDATE);
        if (!updated.getStatus().equals(work.getStatus())) {
            notificationService.create(
                    updated.getProject(),
                    NotificationType.WORK_STATUS_UPDATE,
                    updated.getId(),
                    work.getName()
            );
        }
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(id);
        workRepository.delete(work);
        publish(work, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectWorkDTO> getByProjectId(UUID projectId) {
        return workRepository.findByProjectId(projectId).stream()
                .map(workMapper::toDTO)
                .toList();
    }

    @Transactional
    public ProjectWorkDTO changeStatus(UUID workId, UUID visitId, String newStatus) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(workId);
        var oldStatus = work.getStatus();

        work.setStatus(newStatus);
        updateCompletionPercent(work);

        if (visitId != null) {
            visitService.addWorkToVisit(visitId, work);
        }

        var updated = workRepository.save(work);
        if (!newStatus.equals(oldStatus)) {
            notificationService.create(
                    updated.getProject(),
                    NotificationType.WORK_STATUS_UPDATE,
                    updated.getId(),
                    updated.getName()
            );
        }
        publish(updated, EventWebSocketDTO.Type.UPDATE);
        return workMapper.toDTO(updated);
    }

    private void setStages(ProjectWork work, ProjectWorkDTO dto) {
        work.getStages().clear();
        if (dto.getStages() != null) {
            for (var stageDTO : dto.getStages()) {
                var stage = new ProjectWorkStage();
                stageMapper.update(stage, stageDTO);
                stage.setWork(work);
                work.getStages().add(stage);
            }
        }
    }

    private void updateCompletionPercent(ProjectWork work) {
        var stages = work.getStages();
        if (stages.isEmpty()) {
            work.setCompletionPercent("DONE".equalsIgnoreCase(work.getStatus()) ? 100 : 0);
            return;
        }
        long done = stages.stream().filter(s -> "DONE".equalsIgnoreCase(s.getStatus())).count();
        work.setCompletionPercent((int) (done * 100 / stages.size()));
    }

    private ProjectWork saveAndPublish(ProjectWork work, EventWebSocketDTO.Type type) throws ApiException {
        var saved = workRepository.save(work);
        publish(saved, type);
        return saved;
    }

    private void publish(ProjectWork work, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-work",
                type,
                ActionType.WORK,
                workMapper.toDTO(work),
                Map.of("name", work.getName(), "projectId", work.getProject().getId())
        );
    }
}
