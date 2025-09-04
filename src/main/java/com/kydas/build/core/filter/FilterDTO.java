package com.kydas.build.core.filter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FilterDTO<F> {
    @NotNull
    private Integer limit;

    @NotNull
    private Integer offset;

    @Valid
    private FilterOrderDTO order;

    @NotNull
    private F filter;
}
