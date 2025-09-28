package com.kydas.build.projects.services;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.files.FileDTO;
import com.kydas.build.files.FileRepository;
import com.kydas.build.projects.dto.ProjectWorkCommentDTO;
import com.kydas.build.projects.entities.ProjectWorkComment;
import com.kydas.build.projects.mappers.ProjectWorkCommentMapper;
import com.kydas.build.projects.repositories.ProjectWorkCommentRepository;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectWorkCommentService extends BaseService<ProjectWorkComment, ProjectWorkCommentDTO> {

    private final ProjectWorkCommentRepository commentRepository;
    private final ProjectWorkRepository workRepository;
    private final ProjectWorkCommentMapper commentMapper;
    private final EventPublisher eventPublisher;
    private final SecurityContext securityContext;
    private final FileRepository fileRepository;

    public ProjectWorkCommentService(ProjectWorkCommentRepository commentRepository,
                                     ProjectWorkRepository workRepository,
                                     ProjectWorkCommentMapper commentMapper,
                                     EventPublisher eventPublisher,
                                     SecurityContext securityContext,
                                     FileRepository fileRepository) {
        super(ProjectWorkComment.class);
        this.commentRepository = commentRepository;
        this.workRepository = workRepository;
        this.commentMapper = commentMapper;
        this.eventPublisher = eventPublisher;
        this.securityContext = securityContext;
        this.fileRepository = fileRepository;
    }

    @Override
    public ProjectWorkComment makeEntity(ProjectWorkCommentDTO dto) {
        var comment = new ProjectWorkComment();
        return commentMapper.update(comment, dto);
    }

    @Transactional
    @Override
    public ProjectWorkComment create(ProjectWorkCommentDTO dto) throws ApiException {
        var work = workRepository.findByIdOrElseThrow(dto.getWorkId());
        var comment = makeEntity(dto);
        comment.setWork(work);
        comment.setAuthor(securityContext.getCurrentUser());
        updateFiles(comment, dto.getFiles());
        return saveAndPublish(comment, EventWebSocketDTO.Type.CREATE);
    }

    @Transactional
    @Override
    public ProjectWorkComment update(ProjectWorkCommentDTO dto) throws ApiException {
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

    public List<ProjectWorkCommentDTO> getByWorkId(UUID workId) {
        return commentRepository.findByWorkId(workId).stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    private void updateFiles(ProjectWorkComment comment,  List<FileDTO> fileDTOs) {
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

    private ProjectWorkComment saveAndPublish(ProjectWorkComment comment, EventWebSocketDTO.Type type) throws ApiException {
        var saved = commentRepository.save(comment);
        publish(saved, type);
        return saved;
    }

    private void publish(ProjectWorkComment comment, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-work-comment",
                type,
                ActionType.WORK,
                commentMapper.toDTO(comment),
                Map.of("workId", comment.getWork().getId().toString(), "projectId", comment.getWork().getProject().getId())
        );
    }
}
