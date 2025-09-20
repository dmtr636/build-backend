package com.kydas.build.core.crud;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.filter.FilterV2Request;
import com.kydas.build.core.response.OkResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BaseController<E, DTO> {
    @Autowired
    private BaseService<E, DTO> service;
    @Autowired
    private BaseMapper<E, DTO> mapper;

    @GetMapping
    @Operation(summary = "Получение списка всех объектов")
    public List<DTO> getAll() throws ApiException {
        var entities = service.getAll();
        return entities.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/count")
    @Operation(summary = "Общее количество объектов")
    public long getCountAll() {
        return service.getAllCount();
    }

    @PostMapping
    @Operation(summary = "Создание объекта")
    public DTO create(@RequestBody @Valid DTO dto) throws ApiException {
        var entity = service.create(dto);
        return mapper.toDTO(entity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение объекта по ID")
    public DTO getById(@PathVariable UUID id) throws ApiException {
        var entity = service.getById(id);
        return mapper.toDTO(entity);
    }

    @PutMapping
    @Operation(summary = "Обновление объекта")
    public DTO update(@RequestBody @Valid DTO dto) throws ApiException {
        var entity = service.update(dto);
        return mapper.toDTO(entity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объекта")
    public OkResponse delete(@PathVariable UUID id) throws ApiException {
        service.delete(id);
        return new OkResponse();
    }

    @PostMapping("/filter")
    @Operation(summary = "Фильтрация")
    public List<DTO> filterEntities(@RequestBody @Valid FilterV2Request filterRequest) {
        var entities = service.getByFilter(filterRequest);
        return entities.stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
