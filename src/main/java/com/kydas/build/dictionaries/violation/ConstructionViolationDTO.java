package com.kydas.build.dictionaries.violation;

import com.kydas.build.core.crud.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Accessors(chain = true)
public class ConstructionViolationDTO extends BaseDTO {
    @Nullable
    private String category;

    @NotBlank
    private String kind;

    @NotBlank
    private String severityType;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer remediationDueDays;
}
