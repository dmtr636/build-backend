package com.kydas.build.materials.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import com.kydas.build.files.FileDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PassportQualityDTO extends BaseDTO {
    private UUID materialId;
    private String manufacturer;
    private String consumerNameAndAddress;
    private String contractNumber;
    private String productNameAndGrade;
    private String batchNumber;
    private Integer batchCount;
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate manufactureDate;
    private Integer shippedQuantity;
    private String labChief;
    private List<FileDTO> files = new ArrayList<>();
    private List<FileDTO> images = new ArrayList<>();
}
