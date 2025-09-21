package com.kydas.build.dictionaries.violation;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConstructionViolationMapper extends BaseMapper<ConstructionViolation, ConstructionViolationDTO> {

    @Override
    ConstructionViolationDTO toDTO(ConstructionViolation entity);

    @Override
    @Mapping(target = "version", ignore = true)
    ConstructionViolation update(@MappingTarget ConstructionViolation entity, ConstructionViolationDTO dto);
}
