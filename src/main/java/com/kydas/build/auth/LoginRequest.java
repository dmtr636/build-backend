package com.kydas.build.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotNull
    private Boolean rememberMe;
}
