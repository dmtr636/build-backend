package com.kydas.build.dictionaries.documents;

import com.kydas.build.core.crud.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class NormativeDocumentDTO extends BaseDTO {
    @NotBlank
    private String regulation;

    @NotBlank
    private String name;
}
