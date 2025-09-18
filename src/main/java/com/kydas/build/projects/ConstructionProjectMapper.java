package com.kydas.build.projects;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.organizations.Organization;
import com.kydas.build.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConstructionProjectMapper extends BaseMapper<ConstructionProject, ConstructionProjectDTO> {

    @Override
    @Mapping(target = "responsibleUserId", source = "responsible.id")
    @Mapping(target = "customerOrganizationId", source = "customer.id")
    @Mapping(target = "contractorOrganizationId", source = "contractor.id")
    ConstructionProjectDTO toDTO(ConstructionProject entity);

    @Override
    @Mapping(target = "responsible", source = "responsibleUserId", qualifiedByName = "mapUserIdToEntity")
    @Mapping(target = "customer", source = "customerOrganizationId", qualifiedByName = "mapOrganizationIdToEntity")
    @Mapping(target = "contractor", source = "contractorOrganizationId", qualifiedByName = "mapOrganizationIdToEntity")
    @Mapping(target = "createdAt", ignore = true)
    ConstructionProject update(@MappingTarget ConstructionProject entity, ConstructionProjectDTO dto);

    @Named("mapUserIdToEntity")
    default User mapUserIdToEntity(String userId) {
        if (userId == null) return null;
        var user = new User();
        user.setId(UUID.fromString(userId));
        return user;
    }

    @Named("mapOrganizationIdToEntity")
    default Organization mapOrganizationIdToEntity(String orgId) {
        if (orgId == null) return null;
        var org = new Organization();
        org.setId(UUID.fromString(orgId));
        return org;
    }
}
