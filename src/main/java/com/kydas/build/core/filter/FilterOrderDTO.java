package com.kydas.build.core.filter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FilterOrderDTO {
    @NotBlank
    private String field;

    @NotNull
    private Direction direction;

    public enum Direction {ASC, DESC}
}
