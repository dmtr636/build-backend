package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.projects.dto.components.ProjectWorkVersionDTO;
import com.kydas.build.projects.entities.ProjectWorkVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectWorkVersionMapper extends BaseMapper<ProjectWorkVersion, ProjectWorkVersionDTO> {
    @Override
    @Mapping(target = "workId", source = "work.id")
    ProjectWorkVersionDTO toDTO(ProjectWorkVersion version);

    @Override
    @Mapping(target = "work", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectWorkVersion update(@MappingTarget ProjectWorkVersion entity, ProjectWorkVersionDTO dto);
}
