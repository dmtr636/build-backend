package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectViolation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectViolationRepository extends BaseRepository<ProjectViolation> {
    List<ProjectViolation> findByProjectId(UUID projectId);
}
