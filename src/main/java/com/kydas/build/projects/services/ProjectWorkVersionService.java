package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.projects.dto.components.ProjectWorkVersionDTO;
import com.kydas.build.projects.entities.ProjectWork;
import com.kydas.build.projects.entities.ProjectWorkVersion;
import com.kydas.build.projects.mappers.ProjectWorkVersionMapper;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import com.kydas.build.projects.repositories.ProjectWorkVersionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectWorkVersionService extends BaseService<ProjectWorkVersion, ProjectWorkVersionDTO> {

    private final ProjectWorkVersionRepository versionRepository;
    private final ProjectWorkRepository workRepository;
    private final ProjectWorkVersionMapper versionMapper;
    private final EventPublisher eventPublisher;

    public ProjectWorkVersionService(ProjectWorkVersionRepository versionRepository,
                                     ProjectWorkRepository workRepository,
                                     ProjectWorkVersionMapper versionMapper,
                                     EventPublisher eventPublisher) {
        super(ProjectWorkVersion.class);
        this.versionRepository = versionRepository;
        this.workRepository = workRepository;
        this.versionMapper = versionMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ProjectWorkVersion makeEntity(ProjectWorkVersionDTO dto) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(dto.getWorkId());
        if (dto.isActive()) {
            List<ProjectWorkVersion> toDeactivate = work.getWorkVersions().stream()
                    .filter(ProjectWorkVersion::getActive)
                    .collect(Collectors.toList());

            toDeactivate.forEach(v -> v.setActive(false));
            versionRepository.saveAll(toDeactivate);
        }
        var version = versionMapper.update(new ProjectWorkVersion(), dto);
        version.setWork(work);
        version.setVersionNumber(calculateNextVersionNumber(work));
        return version;
    }

    @Transactional
    @Override
    public ProjectWorkVersion create(ProjectWorkVersionDTO dto) throws ApiException {
        var version = makeEntity(dto);
        var saved = versionRepository.save(version);
        publish(saved, EventWebSocketDTO.Type.CREATE);
        return saved;
    }

    @Transactional
    @Override
    public ProjectWorkVersion update(ProjectWorkVersionDTO dto) throws ApiException {
        var version = versionRepository.findByIdOrElseThrow(dto.getId());

        boolean wasActive = version.getActive();
        versionMapper.update(version, dto);

        if (version.getActive() && !wasActive) {
            var work = version.getWork();
            List<ProjectWorkVersion> toDeactivate = work.getWorkVersions().stream()
                    .filter(v -> !v.getId().equals(version.getId()) && v.getActive())
                    .collect(Collectors.toList());
            toDeactivate.forEach(v -> v.setActive(false));
            versionRepository.saveAll(toDeactivate);
        }

        var updated = versionRepository.save(version);
        publish(updated, EventWebSocketDTO.Type.UPDATE);
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID versionId) throws ApiException {
        var version = versionRepository.findByIdOrElseThrow(versionId);
        versionRepository.delete(version);
        publish(version, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectWorkVersionDTO> getByWorkId(UUID workId) {
        return versionRepository.findByWorkIdOrderByVersionNumberDesc(workId).stream()
                .map(versionMapper::toDTO)
                .toList();
    }

    private int calculateNextVersionNumber(ProjectWork work) {
        return work.getWorkVersions().stream()
                .mapToInt(ProjectWorkVersion::getVersionNumber)
                .max()
                .orElse(0) + 1;
    }

    private void publish(ProjectWorkVersion version, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-work-version",
                type,
                ActionType.WORK,
                versionMapper.toDTO(version),
                Map.of("versionNumber", version.getVersionNumber(), "projectId", version.getWork().getProject().getId())
        );
    }
}
