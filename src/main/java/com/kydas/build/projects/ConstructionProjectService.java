package com.kydas.build.projects;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConstructionProjectService extends BaseService<ConstructionProject, ConstructionProjectDTO> {

    private final ConstructionProjectRepository projectRepository;
    private final ConstructionProjectMapper projectMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public ConstructionProjectService(
            ConstructionProjectRepository projectRepository,
            ConstructionProjectMapper projectMapper,
            EventPublisher eventPublisher
    ) {
        super(ConstructionProject.class);
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ConstructionProject makeEntity(ConstructionProjectDTO dto) {
        var project = new ConstructionProject();
        return projectMapper.update(project, dto);
    }

    @Override
    public ConstructionProject create(ConstructionProjectDTO dto) throws ApiException {
        var project = makeEntity(dto);
        var saved = projectRepository.save(project);
        eventPublisher.publish("construction-project", EventWebSocketDTO.Type.CREATE, projectMapper.toDTO(saved));
        return saved;
    }

    @Override
    public ConstructionProject update(ConstructionProjectDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getId());
        projectMapper.update(project, dto);
        var updated = projectRepository.save(project);
        eventPublisher.publish("construction-project", EventWebSocketDTO.Type.UPDATE, projectMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("construction-project", EventWebSocketDTO.Type.DELETE, projectMapper.toDTO(project));
        projectRepository.delete(project);
    }
}
