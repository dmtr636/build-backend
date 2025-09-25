package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.projects.dto.components.ProjectWorkDTO;
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
import java.util.UUID;

@Service
public class ProjectWorkService extends BaseService<ProjectWork, ProjectWorkDTO> {

    private final ProjectWorkRepository workRepository;
    private final ProjectRepository projectRepository;
    private final ProjectWorkMapper workMapper;
    private final ProjectWorkStageMapper stageMapper;
    private final EventPublisher eventPublisher;

    public ProjectWorkService(ProjectWorkRepository workRepository,
                              ProjectRepository projectRepository,
                              ProjectWorkMapper workMapper,
                              ProjectWorkStageMapper stageMapper,
                              EventPublisher eventPublisher) {
        super(ProjectWork.class);
        this.workRepository = workRepository;
        this.projectRepository = projectRepository;
        this.workMapper = workMapper;
        this.stageMapper = stageMapper;
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
        return saveAndPublish(work, EventWebSocketDTO.Type.CREATE);
    }

    @Transactional
    @Override
    public ProjectWork update(ProjectWorkDTO dto) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(dto.getId());
        workMapper.update(work, dto);
        setStages(work, dto);
        updateCompletionPercent(work);
        return saveAndPublish(work, EventWebSocketDTO.Type.UPDATE);
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
                workMapper.toDTO(work),
                Map.of("name", work.getName())
        );
    }
}
