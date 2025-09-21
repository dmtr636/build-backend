package com.kydas.build.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
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
    private String lastName;

    @Nullable
    private String firstName;

    @Nullable
    private String patronymic;

    @Nullable
    private String messenger;

    @Nullable
    private String email;

    @Nullable
    private String workPhone;

    @Nullable
    private String personalPhone;

    @Nullable
    private String imageId;

    @Nullable
    private String organizationId;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant updatedAt;

    private Map<String, Object> info = Map.of();
}
