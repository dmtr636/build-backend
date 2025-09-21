package com.kydas.build.dictionaries.documents;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NormativeDocumentMapper extends BaseMapper<NormativeDocument, NormativeDocumentDTO> {

    @Override
    NormativeDocumentDTO toDTO(NormativeDocument entity);

    @Override
    @Mapping(target = "version", ignore = true)
    NormativeDocument update(@MappingTarget NormativeDocument entity, NormativeDocumentDTO dto);
}
