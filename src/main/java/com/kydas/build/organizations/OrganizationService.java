package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.users.User;
import com.kydas.build.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrganizationService extends BaseService<Organization, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMapper organizationMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository,
                               UserRepository userRepository,
                               OrganizationMapper organizationMapper,
                               EventPublisher eventPublisher) {
        super(Organization.class);
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMapper = organizationMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Organization makeEntity(OrganizationDTO organizationDTO) {
        var organization = new Organization();
        organization = organizationMapper.update(organization, organizationDTO);
        return organization;
    }

    @Override
    public Organization create(OrganizationDTO organizationDTO) throws ApiException {
        var organization = makeEntity(organizationDTO);
        var saved = organizationRepository.save(organization);
        publish("organization", saved, EventWebSocketDTO.Type.CREATE);
        return saved;
    }

    @Override
    public Organization update(OrganizationDTO organizationDTO) throws ApiException {
        var organization = organizationRepository.findByIdOrElseThrow(organizationDTO.getId());
        organizationMapper.update(organization, organizationDTO);
        var updated = organizationRepository.save(organization);
        publish("organization", updated, EventWebSocketDTO.Type.UPDATE);
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var organization = organizationRepository.findByIdOrElseThrow(id);
        publish("organization", organization, EventWebSocketDTO.Type.DELETE);
        organizationRepository.delete(organization);
    }

    @Transactional
    public void addEmployees(UUID organizationId, List<String> employeeIds) throws ApiException {
        var organization = organizationRepository.findByIdOrElseThrow(organizationId);
        List<User> users = userRepository.findAllById(
                employeeIds.stream().map(UUID::fromString).toList()
        );
        for (User u : users) {
            if (!organization.getEmployees().contains(u)) {
                u.setOrganization(organization);
                organization.getEmployees().add(u);
            }
        }
        publish("organization-employees", organization, EventWebSocketDTO.Type.UPDATE);
    }

    @Transactional
    public void removeEmployees(UUID organizationId, List<String> employeeIds) throws ApiException {
        var organization = organizationRepository.findByIdOrElseThrow(organizationId);
        organization.getEmployees().removeIf(u -> {
            boolean toRemove = employeeIds.contains(u.getId().toString());
            if (toRemove) {
                u.setOrganization(null);
            }
            return toRemove;
        });
        publish("organization-employees", organization, EventWebSocketDTO.Type.UPDATE);
    }

    private void publish(String name, Organization organization, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                name,
                type,
                ActionType.SYSTEM,
                organizationMapper.toDTO(organization),
                Map.of("name", organization.getName())
        );
    }
}
