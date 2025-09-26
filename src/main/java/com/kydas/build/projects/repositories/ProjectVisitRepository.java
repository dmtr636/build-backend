package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectVisit;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectVisitRepository extends BaseRepository<ProjectVisit> {
    List<ProjectVisit> findByProjectId(UUID projectId);

    Optional<ProjectVisit> findByProjectIdAndUserIdAndVisitDateBetween(UUID projectId, UUID userId, Instant start, Instant end);
}
