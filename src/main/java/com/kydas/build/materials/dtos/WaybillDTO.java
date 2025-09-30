package com.kydas.build.materials.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import com.kydas.build.files.FileDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WaybillDTO extends BaseDTO {
    private UUID materialId;
    private String materialName;
    private String receiver;
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant deliveryDateTime;
    private UUID projectWorkId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String projectWorkName;
    private String invoiceNumber;
    private Double volume;
    private Double netWeight;
    private Double grossWeight;
    private Integer packageCount;
    private boolean laboratoryAnalysisRequired;
    private List<FileDTO> files = new ArrayList<>();
    private List<FileDTO> images = new ArrayList<>();
}
