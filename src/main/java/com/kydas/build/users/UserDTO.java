package com.kydas.build.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.core.crud.BaseDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends BaseDTO {
    @NotBlank
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private User.Role role;

    private Boolean enabled = true;

    @Nullable
    private String position;

    @Nullable
    private String name;

    @Nullable
    private String imageId;

    private String createDate;

    private String updateDate;

    private Map<String, Object> info = Map.of();
}

