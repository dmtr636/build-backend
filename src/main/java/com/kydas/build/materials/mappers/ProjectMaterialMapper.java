package com.kydas.build.materials.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.materials.dtos.ProjectMaterialDTO;
import com.kydas.build.materials.entities.ProjectMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {WaybillMapper.class, PassportQualityMapper.class})
public interface ProjectMaterialMapper extends BaseMapper<ProjectMaterial, ProjectMaterialDTO> {

    @Override
    @Mapping(target = "projectId", source = "project.id")
    ProjectMaterialDTO toDTO(ProjectMaterial entity);

    @Override
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "waybill", ignore = true)
    @Mapping(target = "passportQuality", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProjectMaterial update(@MappingTarget ProjectMaterial entity, ProjectMaterialDTO dto);
}

