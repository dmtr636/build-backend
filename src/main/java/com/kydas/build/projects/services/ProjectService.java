package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.dto.ProjectDTO;
import com.kydas.build.projects.entities.Project;
import com.kydas.build.projects.mappers.ProjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class ProjectService extends BaseService<Project, ProjectDTO> {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectRelationsUpdater entitySynchronizer;
    private final EventPublisher eventPublisher;
    private final Random random = new Random();

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          ProjectRelationsUpdater entitySynchronizer,
                          EventPublisher eventPublisher) {
        super(Project.class);
        this.projectRepository = projectRepository;
        this.entitySynchronizer = entitySynchronizer;
        this.projectMapper = projectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Project makeEntity(ProjectDTO dto) {
        var project = new Project();
        projectMapper.update(project, dto);
        if (project.getObjectNumber() == null) {
            project.setObjectNumber(generateUniqueObjectNumber());
        }
        entitySynchronizer.updateProjectRelations(project, dto);
        return project;
    }

    @Override
    public Project create(ProjectDTO dto) throws ApiException {
        var project = makeEntity(dto);
        var saved = projectRepository.save(project);
        eventPublisher.publish("project", EventWebSocketDTO.Type.CREATE, ActionType.WORK, projectMapper.toDTO(saved));
        return saved;
    }

    @Override
    @Transactional
    public Project update(ProjectDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getId());
        projectMapper.update(project, dto);
        entitySynchronizer.updateProjectRelations(project, dto);
        var updated = projectRepository.save(project);
        eventPublisher.publish("project", EventWebSocketDTO.Type.UPDATE, ActionType.WORK, projectMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("project", EventWebSocketDTO.Type.DELETE, ActionType.WORK, projectMapper.toDTO(project));
        projectRepository.delete(project);
    }

    private String generateUniqueObjectNumber() {
        String code;
        do {
            int number = 100000 + random.nextInt(900000);
            code = String.valueOf(number);
        } while (projectRepository.existsByObjectNumber(code));
        return code;
    }
}
