package com.kydas.build.dictionaries.work.stage;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.dictionaries.work.ConstructionWork;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConstructionWorkStageService extends BaseService<ConstructionWorkStage, ConstructionWorkStageDTO> {
    private final ConstructionWorkStageRepository workStageRepository;
    private final ConstructionWorkStageMapper workStageMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public ConstructionWorkStageService(ConstructionWorkStageRepository workStageRepository,
                                        ConstructionWorkStageMapper workStageMapper,
                                        EventPublisher eventPublisher) {
        super(ConstructionWorkStage.class);
        this.workStageRepository = workStageRepository;
        this.workStageMapper = workStageMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ConstructionWorkStage makeEntity(ConstructionWorkStageDTO dto) {
        var stage = new ConstructionWorkStage();
        stage = workStageMapper.update(stage, dto);
        var work = new ConstructionWork();
        work.setId(dto.getWorkId());
        stage.setWork(work);
        return stage;
    }

    @Override
    public ConstructionWorkStage create(ConstructionWorkStageDTO dto) throws ApiException {
        var stage = makeEntity(dto);
        var saved = workStageRepository.save(stage);
        eventPublisher.publish("construction-work-stage", EventWebSocketDTO.Type.CREATE, ActionType.SYSTEM, workStageMapper.toDTO(saved));
        return saved;
    }

    @Override
    public ConstructionWorkStage update(ConstructionWorkStageDTO dto) throws ApiException {
        var stage = workStageRepository.findByIdOrElseThrow(dto.getId());
        workStageMapper.update(stage, dto);
        var work = new ConstructionWork();
        work.setId(dto.getWorkId());
        stage.setWork(work);
        var updated = workStageRepository.save(stage);
        eventPublisher.publish("construction-work-stage", EventWebSocketDTO.Type.UPDATE, ActionType.SYSTEM, workStageMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var constructionWork = workStageRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("construction-work-stage", EventWebSocketDTO.Type.DELETE, ActionType.SYSTEM, workStageMapper.toDTO(constructionWork));
        workStageRepository.delete(constructionWork);
    }

    public List<ConstructionWorkStageDTO> search(UUID workId) {
        return workStageRepository.findByWorkId(workId).stream()
                .map(workStageMapper::toDTO)
                .toList();
    }
}
