package com.kydas.build.dictionaries.work;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.CONSTRUCTION_WORKS_ENDPOINT)
@Tag(name = "Сервис перечня работ")
public class ConstructionWorkController extends BaseController<ConstructionWork, ConstructionWorkDTO> {
}
