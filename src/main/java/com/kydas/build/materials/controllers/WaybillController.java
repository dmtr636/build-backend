package com.kydas.build.materials.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.materials.dtos.WaybillDTO;
import com.kydas.build.materials.entities.Waybill;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.PROJECTS_MATERIALS_WAYBILLS)
@Tag(name = "Сервис ТТН для материалов объекта")
public class WaybillController extends BaseController<Waybill, WaybillDTO> {
}
