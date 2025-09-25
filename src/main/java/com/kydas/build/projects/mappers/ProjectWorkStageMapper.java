package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.projects.dto.components.ProjectWorkStageDTO;
import com.kydas.build.projects.entities.ProjectWorkStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectWorkStageMapper extends BaseMapper<ProjectWorkStage, ProjectWorkStageDTO> {
    @Override
    ProjectWorkStageDTO toDTO(ProjectWorkStage entity);

    @Override
    @Mapping(target = "work", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectWorkStage update(@MappingTarget ProjectWorkStage entity, ProjectWorkStageDTO dto);
}
