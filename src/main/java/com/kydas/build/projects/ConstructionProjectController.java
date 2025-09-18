package com.kydas.build.projects;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.PROJECTS_ENDPOINT)
@Tag(name = "Сервис строительных объектов")
public class ConstructionProjectController extends BaseController<ConstructionProject, ConstructionProjectDTO> {
}
