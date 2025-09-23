package com.kydas.build.projects.mappers;

import com.kydas.build.projects.dto.components.ProjectImageDTO;
import com.kydas.build.projects.entities.ProjectImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectImageMapper {
    @Mapping(target = "fileId", source = "file.id")
    @Mapping(target = "caption", source = "caption")
    ProjectImageDTO toDTO(ProjectImage entity);
}

