package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectViolationComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectViolationCommentRepository extends BaseRepository<ProjectViolationComment> {
    List<ProjectViolationComment> findByViolationId(UUID violationId);
}

