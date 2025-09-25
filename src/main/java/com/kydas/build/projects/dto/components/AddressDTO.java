package com.kydas.build.projects.dto.components;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    @NotBlank
    private String city;

    private String street;

    private String house;
}
