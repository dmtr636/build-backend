package com.kydas.build.checklists.repositories;

import com.kydas.build.checklists.entities.ChecklistTemplate;
import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.crud.BaseRepository;

import java.util.Optional;

public interface ChecklistTemplateRepository extends BaseRepository<ChecklistTemplate> {
    Optional<ChecklistTemplate> findByType(ChecklistFormType type);
}
