package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.files.FileDTO;
import com.kydas.build.files.FileRepository;
import com.kydas.build.projects.dto.ProjectViolationCommentDTO;
import com.kydas.build.projects.entities.ProjectViolationComment;
import com.kydas.build.projects.mappers.ProjectViolationCommentMapper;
import com.kydas.build.projects.repositories.ProjectViolationCommentRepository;
import com.kydas.build.projects.repositories.ProjectViolationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class ProjectViolationCommentService extends BaseService<ProjectViolationComment, ProjectViolationCommentDTO> {

    private final ProjectViolationCommentRepository commentRepository;
    private final ProjectViolationRepository violationRepository;
    private final ProjectViolationCommentMapper commentMapper;
    private final EventPublisher eventPublisher;
    private final SecurityContext securityContext;
    private final FileRepository fileRepository;

    public ProjectViolationCommentService(ProjectViolationCommentRepository commentRepository,
                                          ProjectViolationRepository violationRepository,
                                          ProjectViolationCommentMapper commentMapper,
                                          EventPublisher eventPublisher,
                                          SecurityContext securityContext,
                                          FileRepository fileRepository) {
        super(ProjectViolationComment.class);
        this.commentRepository = commentRepository;
        this.violationRepository = violationRepository;
        this.commentMapper = commentMapper;
        this.eventPublisher = eventPublisher;
        this.securityContext = securityContext;
        this.fileRepository = fileRepository;
    }

    @Override
    public ProjectViolationComment makeEntity(ProjectViolationCommentDTO dto) {
        var comment = new ProjectViolationComment();
        return commentMapper.update(comment, dto);
    }

    @Transactional
    @Override
    public ProjectViolationComment create(ProjectViolationCommentDTO dto) throws ApiException {
        var violation = violationRepository.findByIdOrElseThrow(dto.getViolationId());
        var comment = makeEntity(dto);
        comment.setViolation(violation);
        comment.setAuthor(securityContext.getCurrentUser());
        updateFiles(comment, dto.getFiles());
        return saveAndPublish(comment, EventWebSocketDTO.Type.CREATE);
    }

    @Transactional
    @Override
    public ProjectViolationComment update(ProjectViolationCommentDTO dto) throws ApiException {
        var comment = commentRepository.findByIdOrElseThrow(dto.getId());
        commentMapper.update(comment, dto);
        updateFiles(comment, dto.getFiles());
        return saveAndPublish(comment, EventWebSocketDTO.Type.UPDATE);
    }

    @Transactional
    @Override
    public void delete(UUID commentId) throws ApiException {
        var comment = commentRepository.findByIdOrElseThrow(commentId);
        commentRepository.delete(comment);
        publish(comment, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectViolationCommentDTO> getByViolationId(UUID violationId) {
        return commentRepository.findByViolationId(violationId).stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    private void updateFiles(ProjectViolationComment comment, List<FileDTO> fileDTOs) {
        if (fileDTOs != null) {
            var files = fileDTOs.stream()
                    .map(FileDTO::getId)
                    .map(fileRepository::getReferenceById)
                    .toList();
            comment.setFiles(new ArrayList<>(files));
        } else {
            comment.getFiles().clear();
        }
    }

    private ProjectViolationComment saveAndPublish(ProjectViolationComment comment, EventWebSocketDTO.Type type) throws ApiException {
        var saved = commentRepository.save(comment);
        publish(saved, type);
        return saved;
    }

    private void publish(ProjectViolationComment comment, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-violation-comment",
                type,
                ActionType.WORK,
                commentMapper.toDTO(comment),
                Map.of("violationId", comment.getViolation().getId().toString())
        );
    }
}

