package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.projects.dto.ProjectVisitDTO;
import com.kydas.build.projects.entities.ProjectViolation;
import com.kydas.build.projects.entities.ProjectVisit;
import com.kydas.build.projects.entities.ProjectWork;
import com.kydas.build.projects.mappers.ProjectVisitMapper;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.repositories.ProjectVisitRepository;
import com.kydas.build.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectVisitService extends BaseService<ProjectVisit, ProjectVisitDTO> {

    private final ProjectVisitRepository visitRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectVisitMapper visitMapper;
    private final EventPublisher eventPublisher;

    public ProjectVisitService(ProjectVisitRepository visitRepository,
                               ProjectRepository projectRepository,
                               UserRepository userRepository,
                               ProjectVisitMapper visitMapper,
                               EventPublisher eventPublisher) {
        super(ProjectVisit.class);
        this.visitRepository = visitRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.visitMapper = visitMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ProjectVisit makeEntity(ProjectVisitDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getProjectId());
        var user = userRepository.findByIdOrElseThrow(dto.getUser().getId());

        var visit = new ProjectVisit();
        visit.setProject(project);
        visit.setUser(user);
        visit.setVisitDate(dto.getVisitDate() != null ? dto.getVisitDate() : Instant.now());

        return visit;
    }

    @Transactional
    @Override
    public ProjectVisit create(ProjectVisitDTO dto) throws ApiException {
        var visit = makeEntity(dto);
        var saved = visitRepository.save(visit);
        publish(saved, EventWebSocketDTO.Type.CREATE);
        return saved;
    }

    @Transactional
    @Override
    public ProjectVisit update(ProjectVisitDTO dto) throws ApiException {
        var visit = visitRepository.findByIdOrElseThrow(dto.getId());
        var user = userRepository.findByIdOrElseThrow(dto.getUser().getId());
        visitMapper.update(visit, dto);
        visit.setUser(user);
        var updated = visitRepository.save(visit);
        publish(updated, EventWebSocketDTO.Type.UPDATE);
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var visit = visitRepository.findByIdOrElseThrow(id);
        visitRepository.delete(visit);
        publish(visit, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectVisitDTO> getByProject(UUID projectId) {
        return visitRepository.findByProjectId(projectId).stream()
                .map(visitMapper::toDTO)
                .toList();
    }

    @Transactional
    public ProjectVisitDTO getByProjectUserAndDate(UUID projectId, UUID userId, LocalDate date) throws ApiException {
        var start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        var end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        var visit = visitRepository.findByProjectIdAndUserIdAndVisitDateBetween(projectId, userId, start, end)
                .orElseThrow(NotFoundException::new);

        return visitMapper.toDTO(visit);
    }

    @Transactional
    public void addViolationToVisit(UUID visitId, ProjectViolation violation) throws ApiException {
        var visit = visitRepository.findByIdOrElseThrow(visitId);
        if (!visit.getViolations().contains(violation)) {
            visit.getViolations().add(violation);
            visitRepository.save(visit);
        }
    }

    @Transactional
    public void addWorkToVisit(UUID visitId, ProjectWork work) throws ApiException {
        var visit = visitRepository.findByIdOrElseThrow(visitId);
        if (!visit.getWorks().contains(work)) {
            visit.getWorks().add(work);
            visitRepository.save(visit);
        }
    }

    private void publish(ProjectVisit visit, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-visit",
                type,
                ActionType.WORK,
                visitMapper.toDTO(visit),
                Map.of("projectName", visit.getProject().getName(), "projectId", visit.getProject().getId())
        );
    }
}

