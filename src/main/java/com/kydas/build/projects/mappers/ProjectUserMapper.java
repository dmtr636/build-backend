package com.kydas.build.projects.mappers;

import com.kydas.build.projects.dto.components.ProjectUserDTO;
import com.kydas.build.projects.entities.ProjectUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectUserMapper {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "patronymic", source = "user.patronymic")
    @Mapping(target = "position", source = "user.position")
    ProjectUserDTO toDTO(ProjectUser entity);
}
