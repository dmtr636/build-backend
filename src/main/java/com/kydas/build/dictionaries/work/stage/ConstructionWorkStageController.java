package com.kydas.build.dictionaries.work.stage;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.CONSTRUCTION_WORK_STAGES_ENDPOINT)
@Tag(name = "Сервис перечня этапов работ")
public class ConstructionWorkStageController extends BaseController<ConstructionWorkStage, ConstructionWorkStageDTO> {

    private final ConstructionWorkStageService workStageService;

    public ConstructionWorkStageController(ConstructionWorkStageService workStageService) {
        this.workStageService = workStageService;
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск стадий с фильтрацией по параметрам")
    public List<ConstructionWorkStageDTO> search(@RequestParam(required = false) UUID workId) {
        return workStageService.search(workId);
    }
}
