package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectWorkVersion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectWorkVersionRepository extends BaseRepository<ProjectWorkVersion> {
    List<ProjectWorkVersion> findByWorkIdOrderByVersionNumberDesc(UUID workId);
}
