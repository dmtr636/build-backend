package com.kydas.build.projects.mappers;

import com.kydas.build.core.crud.BaseMapper;
import com.kydas.build.organizations.Organization;
import com.kydas.build.projects.dto.ProjectDTO;
import com.kydas.build.projects.dto.components.AddressDTO;
import com.kydas.build.projects.dto.components.ConstructionPeriodDTO;
import com.kydas.build.projects.dto.components.CoordinateDTO;
import com.kydas.build.projects.entities.Project;
import com.kydas.build.projects.entities.embeddable.Address;
import com.kydas.build.projects.entities.embeddable.ConstructionPeriod;
import com.kydas.build.projects.entities.embeddable.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProjectUserMapper.class, ProjectImageMapper.class, ProjectDocumentMapper.class})
public interface ProjectMapper extends BaseMapper<Project, ProjectDTO> {

    @Override
    ProjectDTO toDTO(Project entity);

    @Mapping(target = "objectNumber", ignore = true)
    @Mapping(target = "projectUsers", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "works", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project update(@MappingTarget Project entity, ProjectDTO dto);

    default UUID map(Organization organization) {
        return organization != null ? organization.getId() : null;
    }

    default Organization mapOrganization(UUID id) {
        if (id == null) { return null; }
        var organization = new Organization();
        organization.setId(id);
        return organization;
    }

    AddressDTO toDTO(Address address);

    CoordinateDTO toDTO(Coordinate coordinate);

    ConstructionPeriodDTO toDTO(ConstructionPeriod period);

    Address toEntity(AddressDTO dto);

    Coordinate toEntity(CoordinateDTO dto);

    ConstructionPeriod toEntity(ConstructionPeriodDTO dto);
}
