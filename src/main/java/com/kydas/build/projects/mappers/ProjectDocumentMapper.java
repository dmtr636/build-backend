package com.kydas.build.projects.mappers;

import com.kydas.build.files.FileMapper;
import com.kydas.build.projects.dto.components.ProjectDocumentDTO;
import com.kydas.build.projects.entities.ProjectDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {FileMapper.class})
public interface ProjectDocumentMapper {
    ProjectDocumentDTO toDTO(ProjectDocument entity);
}
