package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.response.OkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.ORGANIZATIONS_ENDPOINT)
@Tag(name = "Сервис организаций")
public class OrganizationController extends BaseController<Organization, OrganizationDTO> {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/{organizationId}/employees")
    @Operation(summary = "Добавление списка сотрудников в организацию")
    public OkResponse addEmployees(
            @PathVariable UUID organizationId,
            @RequestBody @Valid List<String> employeeIds
    ) throws ApiException {
        organizationService.addEmployees(organizationId, employeeIds);
        return new OkResponse();
    }

    @DeleteMapping("/{organizationId}/employees")
    @Operation(summary = "Удаление списка сотрудников из организации")
    public OkResponse removeEmployees(
            @PathVariable UUID organizationId,
            @RequestBody @Valid List<String> employeeIds
    ) throws ApiException {
        organizationService.removeEmployees(organizationId, employeeIds);
        return new OkResponse();
    }
}
