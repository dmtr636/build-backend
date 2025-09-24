package com.kydas.build.projects;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends BaseRepository<Project> {
    boolean existsByObjectNumber(String objectNumber);
}
