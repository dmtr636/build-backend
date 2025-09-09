package com.kydas.build.events;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.core.security.SecurityContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService extends BaseService<Event, EventDTO> {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private SecurityContext securityContext;

    public EventService() {
        super(Event.class);
    }

    @Override
    public Event makeEntity(EventDTO eventDTO) throws ApiException {
        var event = new Event();
        event = eventMapper.update(event, eventDTO);
        return event;
    }

    @Override
    public Event create(EventDTO eventDTO) throws ApiException {
        var event = makeEntity(eventDTO);
        return eventRepository.save(event);
    }

    @Override
    public Event update(EventDTO eventDTO) throws ApiException {
        var event = eventRepository.findById(eventDTO.getId()).orElseThrow(NotFoundException::new);
        eventMapper.update(event, eventDTO);
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        eventRepository.delete(event);
    }
}
