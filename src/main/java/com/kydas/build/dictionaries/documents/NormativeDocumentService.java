package com.kydas.build.dictionaries.documents;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NormativeDocumentService extends BaseService<NormativeDocument, NormativeDocumentDTO> {
    private final NormativeDocumentRepository documentRepository;
    private final NormativeDocumentMapper documentMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public NormativeDocumentService(NormativeDocumentRepository documentRepository,
                                    NormativeDocumentMapper documentMapper,
                                    EventPublisher eventPublisher) {
        super(NormativeDocument.class);
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public NormativeDocument makeEntity(NormativeDocumentDTO normativeDocumentDTO) {
        var normativeDocument = new NormativeDocument();
        normativeDocument = documentMapper.update(normativeDocument, normativeDocumentDTO);
        return normativeDocument;
    }

    @Override
    public NormativeDocument create(NormativeDocumentDTO normativeDocumentDTO) throws ApiException {
        var normativeDocument = makeEntity(normativeDocumentDTO);
        var saved = documentRepository.save(normativeDocument);
        eventPublisher.publish("normative-document", EventWebSocketDTO.Type.CREATE, ActionType.SYSTEM, documentMapper.toDTO(saved));
        return saved;
    }

    @Override
    public NormativeDocument update(NormativeDocumentDTO normativeDocumentDTO) throws ApiException {
        var normativeDocument = documentRepository.findByIdOrElseThrow(normativeDocumentDTO.getId());
        documentMapper.update(normativeDocument, normativeDocumentDTO);
        var updated = documentRepository.save(normativeDocument);
        eventPublisher.publish("normative-document", EventWebSocketDTO.Type.UPDATE, ActionType.SYSTEM, documentMapper.toDTO(updated));
        return updated;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var normativeDocument = documentRepository.findByIdOrElseThrow(id);
        eventPublisher.publish("normative-document", EventWebSocketDTO.Type.DELETE, ActionType.SYSTEM, documentMapper.toDTO(normativeDocument));
        documentRepository.delete(normativeDocument);
    }
}
