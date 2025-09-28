package com.kydas.build.checklists.mappers;

import com.kydas.build.checklists.dto.ChecklistSectionDTO;
import com.kydas.build.checklists.entities.TemplateSection;
import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ChecklistItemAnswerMapper.class})
public interface TemplateSectionMapper extends BaseMapper<TemplateSection, ChecklistSectionDTO> {

    @Override
    @Mapping(target = "items", ignore = true)
    ChecklistSectionDTO toDTO(TemplateSection entity);

    @Override
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "version", ignore = true)
    TemplateSection update(TemplateSection entity, ChecklistSectionDTO checklistSectionDTO);
}

