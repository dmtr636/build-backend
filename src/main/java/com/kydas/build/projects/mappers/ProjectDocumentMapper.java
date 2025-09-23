package com.kydas.build.projects.mappers;

import com.kydas.build.projects.dto.components.ProjectDocumentDTO;
import com.kydas.build.projects.entities.ProjectDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectDocumentMapper {
    @Mapping(target = "fileId", source = "file.id")
    ProjectDocumentDTO toDTO(ProjectDocument entity);
}
