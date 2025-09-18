package com.kydas.build.projects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class ConstructionProjectDTO extends BaseDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String number;

    @NotBlank
    private String address;

    @NotBlank
    private String district;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @Nullable
    private String responsibleUserId;

    @Nullable
    private String customerOrganizationId;

    @Nullable
    private String contractorOrganizationId;

    @Nullable
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate startDate;

    @Nullable
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate endDate;

    @Nullable
    private String imageId;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;
}
