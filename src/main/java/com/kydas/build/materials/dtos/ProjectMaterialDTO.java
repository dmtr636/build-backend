package com.kydas.build.materials.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class ProjectMaterialDTO extends BaseDTO {
    private UUID projectId;
    private WaybillDTO waybill;
    private PassportQualityDTO passportQuality;
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant updatedAt;
}
