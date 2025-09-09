package com.kydas.build.events;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.EVENTS_ENDPOINT)
@Tag(name = "Сервис событий")
public class EventController extends BaseController<Event, EventDTO> {

}
