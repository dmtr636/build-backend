package com.kydas.build.dictionaries.work;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConstructionWorkMapper extends BaseMapper<ConstructionWork, ConstructionWorkDTO> {

    @Override
    ConstructionWorkDTO toDTO(ConstructionWork entity);

    @Override
    @Mapping(target = "version", ignore = true)
    ConstructionWork update(@MappingTarget ConstructionWork entity, ConstructionWorkDTO dto);
}
