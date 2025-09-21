package com.kydas.build.dictionaries.work;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConstructionWorkService extends BaseService<ConstructionWork, ConstructionWorkDTO> {
    private final ConstructionWorkRepository workRepository;
    private final ConstructionWorkMapper workMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public ConstructionWorkService(ConstructionWorkRepository workRepository,
                                   ConstructionWorkMapper workMapper,
                                   EventPublisher eventPublisher) {
        super(ConstructionWork.class);
        this.workRepository = workRepository;
        this.workMapper = workMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ConstructionWork makeEntity(ConstructionWorkDTO constructionWorkDTO) {
        var constructionWork = new ConstructionWork();
        constructionWork = workMapper.update(constructionWork, constructionWorkDTO);
        return constructionWork;
    }

    @Override
    public ConstructionWork create(ConstructionWorkDTO constructionWorkDTO) throws ApiException {
        var constructionWork = makeEntity(constructionWorkDTO);
        var saved = workRepository.save(constructionWork);
        eventPublisher.publish("construction-work", EventWebSocketDTO.Type.CREATE, workMapper.toDTO(saved));
        return saved;
    }

    @Override
    public ConstructionWork update(ConstructionWorkDTO constructionWorkDTO) throws ApiException {
        var constructionWork = workRepository.findByIdOrElseThrow(constructionWorkDTO.getId());
        workMapper.update(constructionWork, constructionWorkDTO);
        var updated = workRepository.save(constructionWork);
        eventPublisher.publish("construction-work", EventWebSocketDTO.Type.UPDATE, workMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var constructionWork = workRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("construction-work", EventWebSocketDTO.Type.DELETE, workMapper.toDTO(constructionWork));
        workRepository.delete(constructionWork);
    }
}
