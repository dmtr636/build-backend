package com.kydas.build.materials.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.files.FileMapper;
import com.kydas.build.materials.dtos.WaybillDTO;
import com.kydas.build.materials.entities.Waybill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {FileMapper.class})
public interface WaybillMapper extends BaseMapper<Waybill, WaybillDTO> {

    @Override
    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "projectWorkId", source = "projectWork.id")
    @Mapping(target = "projectWorkName", source = "projectWork.name")
    WaybillDTO toDTO(Waybill entity);

    @Override
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "projectWork", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "version", ignore = true)
    Waybill update(@MappingTarget Waybill entity, WaybillDTO dto);
}
