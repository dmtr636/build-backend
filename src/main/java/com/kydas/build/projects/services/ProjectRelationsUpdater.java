package com.kydas.build.projects.services;

import com.kydas.build.files.FileRepository;
import com.kydas.build.projects.dto.ProjectDTO;
import com.kydas.build.projects.dto.components.ProjectDocumentDTO;
import com.kydas.build.projects.dto.components.ProjectImageDTO;
import com.kydas.build.projects.dto.components.ProjectUserDTO;
import com.kydas.build.projects.entities.Project;
import com.kydas.build.projects.entities.ProjectDocument;
import com.kydas.build.projects.entities.ProjectImage;
import com.kydas.build.projects.entities.ProjectUser;
import com.kydas.build.projects.entities.embeddable.ProjectUserKey;
import com.kydas.build.users.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProjectRelationsUpdater {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public ProjectRelationsUpdater(UserRepository userRepository,
                                   FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    public void updateProjectRelations(Project project, ProjectDTO dto) {
        syncUsers(project, dto.getProjectUsers());
        syncImages(project, dto.getGallery());
        syncDocuments(project, dto.getDocuments());
    }

    private void syncUsers(Project project, List<ProjectUserDTO> userDTOs) {
        syncCollection(
                project.getProjectUsers(),
                userDTOs,
                ProjectUserDTO::getId,
                u -> u.getUser().getId(),
                (entity, dto) -> {
                    entity.setSide(dto.getSide());
                    entity.setIsResponsible(dto.getIsResponsible());
                },
                dto -> {
                    var user = new ProjectUser();
                    var key = new ProjectUserKey();
                    key.setProjectId(project.getId());
                    key.setUserId(dto.getId());
                    user.setId(key);
                    user.setProject(project);
                    user.setUser(userRepository.getReferenceById(dto.getId()));
                    user.setSide(dto.getSide());
                    user.setIsResponsible(dto.getIsResponsible());
                    return user;
                }
        );
    }

    private void syncImages(Project project, List<ProjectImageDTO> imageDTOs) {
        syncCollection(
                project.getGallery(),
                imageDTOs,
                ProjectImageDTO::getFileId,
                img -> img.getFile().getId(),
                (entity, dto) -> {
                    entity.setCaption(dto.getCaption());
                    entity.setTakenAt(dto.getTakenAt());
                },
                dto -> {
                    var img = new ProjectImage();
                    img.setProject(project);
                    img.setFile(fileRepository.getReferenceById(dto.getFileId()));
                    img.setCaption(dto.getCaption());
                    img.setTakenAt(dto.getTakenAt());
                    return img;
                }
        );
    }

    private void syncDocuments(Project project, List<ProjectDocumentDTO> docDTOs) {
        syncCollection(
                project.getDocuments(),
                docDTOs,
                ProjectDocumentDTO::getFileId,
                doc -> doc.getFile().getId(),
                (entity, dto) -> entity.setDocumentGroup(dto.getDocumentGroup()),
                dto -> {
                    var doc = new ProjectDocument();
                    doc.setProject(project);
                    doc.setFile(fileRepository.getReferenceById(dto.getFileId()));
                    doc.setDocumentGroup(dto.getDocumentGroup());
                    return doc;
                }
        );
    }

    private <T, D, K> void syncCollection(
            Collection<T> entities,
            List<D> dtos,
            Function<D, K> dtoKeyExtractor,
            Function<T, K> entityKeyExtractor,
            BiConsumer<T, D> updater,
            Function<D, T> entityCreator
    ) {
        Map<K, D> dtoMap = dtos != null
                ? dtos.stream().collect(Collectors.toMap(dtoKeyExtractor, d -> d))
                : Collections.emptyMap();

        entities.removeIf(e -> !dtoMap.containsKey(entityKeyExtractor.apply(e)));

        for (D dto : dtoMap.values()) {
            entities.stream()
                    .filter(e -> entityKeyExtractor.apply(e).equals(dtoKeyExtractor.apply(dto)))
                    .findFirst()
                    .ifPresentOrElse(
                            e -> updater.accept(e, dto),
                            () -> entities.add(entityCreator.apply(dto))
                    );
        }
    }
}
