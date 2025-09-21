package com.kydas.build.dictionaries.work;

import com.kydas.build.core.crud.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Accessors(chain = true)
public class ConstructionWorkDTO extends BaseDTO {
    @NotBlank
    private String name;

    @Nullable
    private String unit;

    @Nullable
    private String classificationCode;
}
