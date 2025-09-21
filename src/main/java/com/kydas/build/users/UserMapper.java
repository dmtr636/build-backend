package com.kydas.build.users;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.organizations.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends BaseMapper<User, UserDTO> {
    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "organizationId", source = "organization.id")
    UserDTO toDTO(User entity);

    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationIdToEntity")
    @Mapping(target = "version", ignore = true)
    User update(@MappingTarget User entity, UserDTO userDTO);

    @Named("mapOrganizationIdToEntity")
    default Organization mapOrganizationIdToEntity(String organizationId) {
        if (organizationId == null) return null;
        var organization = new Organization();
        organization.setId(UUID.fromString(organizationId));
        return organization;
    }
}
