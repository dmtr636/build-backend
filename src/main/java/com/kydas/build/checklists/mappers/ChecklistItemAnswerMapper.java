package com.kydas.build.checklists.mappers;

import com.kydas.build.checklists.dto.ChecklistItemAnswerDTO;
import com.kydas.build.checklists.entities.ChecklistItemAnswer;
import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChecklistItemAnswerMapper extends BaseMapper<ChecklistItemAnswer, ChecklistItemAnswerDTO> {

    @Override
    @Mapping(target = "templateItemId", source = "templateItem.id")
    @Mapping(target = "itemNumber", source = "templateItem.itemNumber")
    @Mapping(target = "text", source = "templateItem.text")
    ChecklistItemAnswerDTO toDTO(ChecklistItemAnswer entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instance", ignore = true)
    @Mapping(target = "templateItem", ignore = true)
    @Mapping(target = "version", ignore = true)
    ChecklistItemAnswer update(@MappingTarget ChecklistItemAnswer entity, ChecklistItemAnswerDTO dto);

    default ChecklistItemAnswerDTO toDTOWithTemplate(ChecklistItemAnswer entity) {
        ChecklistItemAnswerDTO dto = toDTO(entity);
        dto.setTemplateItemId(entity.getTemplateItem().getId());
        dto.setItemNumber(entity.getTemplateItem().getItemNumber());
        dto.setText(entity.getTemplateItem().getText());
        return dto;
    }
}

