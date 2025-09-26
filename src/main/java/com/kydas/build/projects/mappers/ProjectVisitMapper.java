package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.projects.dto.ProjectVisitDTO;
import com.kydas.build.projects.entities.ProjectVisit;
import com.kydas.build.users.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProjectViolationMapper.class, ProjectWorkMapper.class, UserMapper.class})
public interface ProjectVisitMapper extends BaseMapper<ProjectVisit, ProjectVisitDTO> {

    @Override
    @Mapping(target = "projectId", source = "project.id")
    ProjectVisitDTO toDTO(ProjectVisit entity);

    @Override
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "violations", ignore = true)
    @Mapping(target = "works", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectVisit update(@MappingTarget ProjectVisit entity, ProjectVisitDTO dto);
}