package com.kydas.build.materials.servicies;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.files.FileServiceHelper;
import com.kydas.build.materials.dtos.WaybillDTO;
import com.kydas.build.materials.entities.Waybill;
import com.kydas.build.materials.mappers.WaybillMapper;
import com.kydas.build.materials.repositories.WaybillRepository;
import com.kydas.build.projects.repositories.ProjectWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WaybillService extends BaseService<Waybill, WaybillDTO> {

    private final WaybillRepository waybillRepository;
    private final ProjectWorkRepository projectWorkRepository;
    private final WaybillMapper waybillMapper;
    private final FileServiceHelper fileHelper;

    public WaybillService(WaybillRepository waybillRepository,
                          ProjectWorkRepository projectWorkRepository,
                          WaybillMapper waybillMapper,
                          FileServiceHelper fileServiceHelper) {
        super(Waybill.class);
        this.waybillRepository = waybillRepository;
        this.projectWorkRepository = projectWorkRepository;
        this.waybillMapper = waybillMapper;
        this.fileHelper = fileServiceHelper;
    }

    @Override
    public Waybill makeEntity(WaybillDTO dto) {
        throw new UnsupportedOperationException("Waybill cannot be created directly");
    }

    @Transactional
    @Override
    public Waybill create(WaybillDTO dto) throws ApiException {
        throw new ApiException().setMessage("Waybill cannot be created directly");
    }

    @Transactional
    @Override
    public Waybill update(WaybillDTO dto) throws ApiException {
        var waybill = waybillRepository.findByIdOrElseThrow(dto.getId());
        if (dto.getProjectWorkId() != null) {
            var work = projectWorkRepository.findByIdOrElseThrow(dto.getProjectWorkId());
            waybill.setProjectWork(work);
        }
        mapFilesAndImages(waybill, dto);
        waybillMapper.update(waybill, dto);
        return waybillRepository.save(waybill);
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        throw new ApiException().setMessage("Waybill cannot be deleted directly");
    }

    private void mapFilesAndImages(Waybill waybill, WaybillDTO dto) {
        waybill.setFiles(fileHelper.mapFiles(dto.getFiles()));
        waybill.setImages(fileHelper.mapFiles(dto.getImages()));
    }
}
