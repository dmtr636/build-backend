package com.kydas.build.cv.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PassportQualityExtractedData {
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
}

