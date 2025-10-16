package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.materials.mappers.WaybillMapper;
import com.kydas.build.projects.dto.ProjectWorkDTO;
import com.kydas.build.projects.entities.ProjectWork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProjectWorkStageMapper.class, ProjectWorkCommentMapper.class, ProjectWorkVersionMapper.class, ProjectViolationMapper.class, WaybillMapper.class})
public interface ProjectWorkMapper extends BaseMapper<ProjectWork, ProjectWorkDTO> {

    @Override
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "plannedVolume", source = "volume.planned")
    @Mapping(target = "actualVolume", source = "volume.actual")
    @Mapping(target = "volumeUnit", source = "volume.unit")
    @Mapping(target = "workVersion", ignore = true)
    ProjectWorkDTO toDTO(ProjectWork entity);

    @Override
    @Mapping(target = "workVersions", ignore = true)
    @Mapping(target = "volume.planned", source = "plannedVolume")
    @Mapping(target = "volume.actual", source = "actualVolume")
    @Mapping(target = "volume.unit", source = "volumeUnit")
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "stages", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectWork update(@MappingTarget ProjectWork entity, ProjectWorkDTO dto);
}
