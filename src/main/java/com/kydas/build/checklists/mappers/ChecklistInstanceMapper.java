package com.kydas.build.checklists.mappers;

import com.kydas.build.checklists.dto.ChecklistInstanceDTO;
import com.kydas.build.checklists.entities.ChecklistInstance;
import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {TemplateSectionMapper.class, ChecklistItemAnswerMapper.class})
public interface ChecklistInstanceMapper extends BaseMapper<ChecklistInstance, ChecklistInstanceDTO> {

    @Override
    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "templateTitle", source = "template.title")
    ChecklistInstanceDTO toDTO(ChecklistInstance entity);

    @Override
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "version", ignore = true)
    ChecklistInstance update(@MappingTarget ChecklistInstance entity, ChecklistInstanceDTO checklistInstanceDTO);
}
