package com.kydas.build.projects.mappers;


import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.files.FileMapper;
import com.kydas.build.projects.dto.ProjectViolationCommentDTO;
import com.kydas.build.projects.entities.ProjectViolationComment;
import com.kydas.build.users.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {FileMapper.class, UserMapper.class})
public interface ProjectViolationCommentMapper extends BaseMapper<ProjectViolationComment, ProjectViolationCommentDTO> {

    @Override
    @Mapping(target = "violationId", source = "violation.id")
    @Mapping(target = "authorId", source = "author.id")
    ProjectViolationCommentDTO toDTO(ProjectViolationComment entity);

    @Override
    @Mapping(target = "violation", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectViolationComment update(@MappingTarget ProjectViolationComment entity, ProjectViolationCommentDTO dto);
}
