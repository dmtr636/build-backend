package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.files.FileMapper;
import com.kydas.build.projects.dto.ProjectViolationDTO;
import com.kydas.build.projects.entities.ProjectViolation;
import com.kydas.build.users.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProjectViolationCommentMapper.class, FileMapper.class, UserMapper.class})
public interface ProjectViolationMapper extends BaseMapper<ProjectViolation, ProjectViolationDTO> {
    @Override
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "latitude", source = "coordinate.latitude")
    @Mapping(target = "longitude", source = "coordinate.longitude")
    @Mapping(target = "visitId", ignore = true)
    ProjectViolationDTO toDTO(ProjectViolation entity);

    @Override
    @Mapping(target = "coordinate.latitude", source = "latitude")
    @Mapping(target = "coordinate.longitude", source = "longitude")
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "resolutionPhotos", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectViolation update(@MappingTarget ProjectViolation entity, ProjectViolationDTO dto);
}
