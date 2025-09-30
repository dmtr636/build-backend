package com.kydas.build.materials.servicies;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.files.FileServiceHelper;
import com.kydas.build.materials.dtos.ProjectMaterialDTO;
import com.kydas.build.materials.entities.PassportQuality;
import com.kydas.build.materials.entities.ProjectMaterial;
import com.kydas.build.materials.entities.Waybill;
import com.kydas.build.materials.mappers.PassportQualityMapper;
import com.kydas.build.materials.mappers.ProjectMaterialMapper;
import com.kydas.build.materials.mappers.WaybillMapper;
import com.kydas.build.materials.repositories.ProjectMaterialRepository;
import com.kydas.build.projects.repositories.ProjectRepository;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectMaterialService extends BaseService<ProjectMaterial, ProjectMaterialDTO> {

    private final ProjectMaterialRepository materialRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMaterialMapper materialMapper;
    private final EventPublisher eventPublisher;
    private final WaybillMapper waybillMapper;
    private final PassportQualityMapper passportMapper;
    private final FileServiceHelper fileHelper;
    private final ProjectWorkRepository projectWorkRepository;

    public ProjectMaterialService(ProjectMaterialRepository materialRepository,
                                  ProjectRepository projectRepository,
                                  ProjectMaterialMapper materialMapper,
                                  EventPublisher eventPublisher,
                                  WaybillMapper waybillMapper,
                                  PassportQualityMapper passportMapper,
                                  FileServiceHelper fileHelper,
                                  ProjectWorkRepository projectWorkRepository) {
        super(ProjectMaterial.class);
        this.materialRepository = materialRepository;
        this.projectRepository = projectRepository;
        this.materialMapper = materialMapper;
        this.eventPublisher = eventPublisher;
        this.waybillMapper = waybillMapper;
        this.passportMapper = passportMapper;
        this.fileHelper = fileHelper;
        this.projectWorkRepository = projectWorkRepository;
    }

    @Override
    public ProjectMaterial makeEntity(ProjectMaterialDTO dto) throws ApiException {
        var project = projectRepository.findByIdOrElseThrow(dto.getProjectId());
        var material = new ProjectMaterial();
        material.setProject(project);
        materialMapper.update(material, dto);

        var waybill = new Waybill();
        waybill.setMaterial(material);
        if (dto.getWaybill() != null) {
            waybillMapper.update(waybill, dto.getWaybill());
            waybill.setFiles(fileHelper.fetchFiles(dto.getWaybill().getFiles()));
            waybill.setImages(fileHelper.fetchFiles(dto.getWaybill().getImages()));
            if (dto.getWaybill().getProjectWorkId() != null) {
                var work = projectWorkRepository.findByIdOrElseThrow(dto.getWaybill().getProjectWorkId());
                waybill.setProjectWork(work);
            }
        }
        material.setWaybill(waybill);

        if (dto.getPassportQuality() != null) {
            var passport = new PassportQuality();
            passport.setMaterial(material);
            passportMapper.update(passport, dto.getPassportQuality());
            passport.setFiles(fileHelper.fetchFiles(dto.getPassportQuality().getFiles()));
            passport.setImages(fileHelper.fetchFiles(dto.getPassportQuality().getImages()));
            material.setPassportQuality(passport);
        }

        return material;
    }

    @Transactional
    @Override
    public ProjectMaterial create(ProjectMaterialDTO dto) throws ApiException {
        var material = makeEntity(dto);
        var saved = materialRepository.save(material);
        publish(saved, EventWebSocketDTO.Type.CREATE);
        return saved;
    }

    @Transactional
    @Override
    public ProjectMaterial update(ProjectMaterialDTO dto) throws ApiException {
        throw new ApiException().setMessage("Updating ProjectMaterial is not allowed");
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var material = materialRepository.findByIdOrElseThrow(id);
        materialRepository.delete(material);
        publish(material, EventWebSocketDTO.Type.DELETE);
    }

    public List<ProjectMaterialDTO> getByProject(UUID projectId) {
        return materialRepository.findByProjectId(projectId).stream()
                .map(materialMapper::toDTO)
                .toList();
    }

    private void publish(ProjectMaterial material, EventWebSocketDTO.Type type) throws ApiException {
        eventPublisher.publish(
                "project-material",
                type,
                ActionType.WORK,
                materialMapper.toDTO(material),
                Map.of(
                        "projectName", material.getProject().getName(),
                        "projectId", material.getProject().getId()
                )
        );
    }
}
