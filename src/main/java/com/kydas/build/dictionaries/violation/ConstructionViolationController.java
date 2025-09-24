package com.kydas.build.dictionaries.violation;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.CONSTRUCTION_VIOLATIONS)
@Tag(name = "Сервис перечня нарушений")
public class ConstructionViolationController extends BaseController<ConstructionViolation, ConstructionViolationDTO> {
}
