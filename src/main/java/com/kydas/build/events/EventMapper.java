package com.kydas.build.events;

import com.kydas.build.core.crud.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper extends BaseMapper<Event, EventDTO> {
    @Override
    EventDTO toDTO(Event entity);

    @Override
    @Mapping(target = "version", ignore = true)
    Event update(@MappingTarget Event entity, EventDTO eventDTO);
}