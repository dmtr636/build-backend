package com.kydas.build.notifications;

import com.kydas.build.notifications.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "read", ignore = true)
    NotificationDTO toDTO(Notification notification);
}
