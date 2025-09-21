package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.users.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface OrganizationMapper extends BaseMapper<Organization, OrganizationDTO> {

    @Override
    @Mapping(target = "employees", source = "employees")
    OrganizationDTO toDTO(Organization entity);

    @Override
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "version", ignore = true)
    Organization update(@MappingTarget Organization entity, OrganizationDTO dto);
}
