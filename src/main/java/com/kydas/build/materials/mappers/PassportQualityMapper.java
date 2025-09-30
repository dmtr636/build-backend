package com.kydas.build.materials.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.materials.dtos.PassportQualityDTO;
import com.kydas.build.materials.entities.PassportQuality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassportQualityMapper extends BaseMapper<PassportQuality, PassportQualityDTO> {

    @Override
    @Mapping(target = "materialId", source = "material.id")
    PassportQualityDTO toDTO(PassportQuality entity);

    @Override
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "version", ignore = true)
    PassportQuality update(@MappingTarget PassportQuality entity, PassportQualityDTO dto);
}

