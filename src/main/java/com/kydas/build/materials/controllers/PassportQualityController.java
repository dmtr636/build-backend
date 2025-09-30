package com.kydas.build.materials.controllers;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.materials.dtos.PassportQualityDTO;
import com.kydas.build.materials.entities.PassportQuality;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.PROJECTS_MATERIALS_PASSPORT_QUALITIES)
@Tag(name = "Сервис паспорта качества для материалов объекта")
public class PassportQualityController extends BaseController<PassportQuality, PassportQualityDTO> {
}
