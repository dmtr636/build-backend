package com.kydas.build.dictionaries.violation;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConstructionViolationService extends BaseService<ConstructionViolation, ConstructionViolationDTO> {
    private final ConstructionViolationRepository violationRepository;
    private final ConstructionViolationMapper violationMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public ConstructionViolationService(ConstructionViolationRepository violationRepository,
                                        ConstructionViolationMapper violationMapper,
                                        EventPublisher eventPublisher) {
        super(ConstructionViolation.class);
        this.violationRepository = violationRepository;
        this.violationMapper = violationMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ConstructionViolation makeEntity(ConstructionViolationDTO constructionViolationDTO) {
        var constructionViolation = new ConstructionViolation();
        constructionViolation = violationMapper.update(constructionViolation, constructionViolationDTO);
        return constructionViolation;
    }

    @Override
    public ConstructionViolation create(ConstructionViolationDTO constructionViolationDTO) throws ApiException {
        var constructionViolation = makeEntity(constructionViolationDTO);
        var saved = violationRepository.save(constructionViolation);
        eventPublisher.publish("construction-violation", EventWebSocketDTO.Type.CREATE, violationMapper.toDTO(saved));
        return saved;
    }

    @Override
    public ConstructionViolation update(ConstructionViolationDTO constructionViolationDTO) throws ApiException {
        var constructionViolation = violationRepository.findByIdOrElseThrow(constructionViolationDTO.getId());
        violationMapper.update(constructionViolation, constructionViolationDTO);
        var updated = violationRepository.save(constructionViolation);
        eventPublisher.publish("construction-violation", EventWebSocketDTO.Type.UPDATE, violationMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var constructionViolation = violationRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("construction-violation", EventWebSocketDTO.Type.DELETE, violationMapper.toDTO(constructionViolation));
        violationRepository.delete(constructionViolation);
    }
}
