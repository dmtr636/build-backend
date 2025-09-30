package com.kydas.build.materials.servicies;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.files.FileServiceHelper;
import com.kydas.build.materials.dtos.PassportQualityDTO;
import com.kydas.build.materials.entities.PassportQuality;
import com.kydas.build.materials.mappers.PassportQualityMapper;
import com.kydas.build.materials.repositories.PassportQualityRepository;
import com.kydas.build.materials.repositories.ProjectMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PassportQualityService extends BaseService<PassportQuality, PassportQualityDTO> {

    private final PassportQualityRepository passportQualityRepository;
    private final ProjectMaterialRepository materialRepository;
    private final PassportQualityMapper passportQualityMapper;
    private final FileServiceHelper fileHelper;

    public PassportQualityService(PassportQualityRepository passportQualityRepository,
                                  ProjectMaterialRepository materialRepository,
                                  PassportQualityMapper passportQualityMapper,
                                  FileServiceHelper fileHelper) {
        super(PassportQuality.class);
        this.passportQualityRepository = passportQualityRepository;
        this.materialRepository = materialRepository;
        this.passportQualityMapper = passportQualityMapper;
        this.fileHelper = fileHelper;
    }

    @Override
    public PassportQuality makeEntity(PassportQualityDTO dto) throws ApiException {
        var material = materialRepository.findByIdOrElseThrow(dto.getMaterialId());
        var passport = new PassportQuality();
        passport.setMaterial(material);
        passport.setFiles(fileHelper.fetchFiles(dto.getFiles()));
        passport.setImages(fileHelper.fetchFiles(dto.getImages()));
        passportQualityMapper.update(passport, dto);
        return passport;
    }

    @Transactional
    @Override
    public PassportQuality create(PassportQualityDTO dto) throws ApiException {
        var passport = makeEntity(dto);
        return passportQualityRepository.save(passport);
    }

    @Transactional
    @Override
    public PassportQuality update(PassportQualityDTO dto) throws ApiException {
        var passport = passportQualityRepository.findByIdOrElseThrow(dto.getId());
        passport.setFiles(fileHelper.mapFiles(dto.getFiles()));
        passport.setImages(fileHelper.mapFiles(dto.getImages()));
        passportQualityMapper.update(passport, dto);
        return passportQualityRepository.save(passport);
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var passport = passportQualityRepository.findByIdOrElseThrow(id);
        passportQualityRepository.delete(passport);
    }
}
