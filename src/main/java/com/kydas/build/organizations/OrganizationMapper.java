package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper extends BaseMapper<Organization, OrganizationDTO> {
    @Override
    @Mapping(target = "employeeIds", source = "employees", qualifiedByName = "mapEmployeesToIds")
    OrganizationDTO toDTO(Organization entity);

    @Override
    @Mapping(target = "employees", ignore = true)
    Organization update(@MappingTarget Organization entity, OrganizationDTO dto);

    @Named("mapEmployeesToIds")
    default List<String> mapEmployeesToIds(List<User> employees) {
        if (employees == null) return List.of();
        return employees.stream()
                .map(u -> u.getId().toString())
                .collect(Collectors.toList());
    }
}
