package com.kydas.build.dictionaries.work.stage;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConstructionWorkStageMapper extends BaseMapper<ConstructionWorkStage, ConstructionWorkStageDTO> {

    @Override
    @Mapping(target = "workId", source = "work.id")
    ConstructionWorkStageDTO toDTO(ConstructionWorkStage entity);

    @Override
    @Mapping(target = "work", ignore = true)
    @Mapping(target = "version", ignore = true)
    ConstructionWorkStage update(@MappingTarget ConstructionWorkStage entity, ConstructionWorkStageDTO dto);
}
