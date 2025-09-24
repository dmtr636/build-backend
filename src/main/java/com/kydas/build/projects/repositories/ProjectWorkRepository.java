package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectWork;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectWorkRepository extends BaseRepository<ProjectWork> {
    List<ProjectWork> findByProjectId(UUID projectId);
}
