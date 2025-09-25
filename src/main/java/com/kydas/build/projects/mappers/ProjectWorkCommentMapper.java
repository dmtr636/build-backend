package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.projects.dto.ProjectWorkCommentDTO;
import com.kydas.build.projects.entities.ProjectWorkComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectWorkCommentMapper extends BaseMapper<ProjectWorkComment, ProjectWorkCommentDTO> {

    @Override
    @Mapping(target = "workId", source = "work.id")
    @Mapping(target = "authorId", source = "author.id")
    ProjectWorkCommentDTO toDTO(ProjectWorkComment entity);

    @Override
    @Mapping(target = "work", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectWorkComment update(@MappingTarget ProjectWorkComment entity, ProjectWorkCommentDTO dto);
}
