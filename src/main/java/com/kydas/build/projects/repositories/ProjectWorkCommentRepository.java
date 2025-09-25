package com.kydas.build.projects.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.projects.entities.ProjectWorkComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectWorkCommentRepository extends BaseRepository<ProjectWorkComment> {
    List<ProjectWorkComment> findByWorkId(UUID workId);
}
