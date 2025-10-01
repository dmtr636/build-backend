package com.kydas.build.cv.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaybillExtractedData {
    private String invoiceNumber;
    private String materialName;
    private String volume;
    private String netWeight;
    private String grossWeight;
    private Integer packageCount;
}
