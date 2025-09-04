package com.kydas.build.core.crud;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class BaseDTO {
    private UUID id;
}

