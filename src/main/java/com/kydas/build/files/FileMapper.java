package com.kydas.build.files;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper extends BaseMapper<File, FileDTO> {
    @Override
    FileDTO toDTO(File entity);

    @Override
    @Mapping(target = "version", ignore = true)
    File update(@MappingTarget File entity, FileDTO dto);
}
