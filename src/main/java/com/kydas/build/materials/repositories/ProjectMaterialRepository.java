package com.kydas.build.materials.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.materials.entities.ProjectMaterial;

import java.util.List;
import java.util.UUID;

public interface ProjectMaterialRepository extends BaseRepository<ProjectMaterial> {
    List<ProjectMaterial> findByProjectId(UUID projectId);
}
