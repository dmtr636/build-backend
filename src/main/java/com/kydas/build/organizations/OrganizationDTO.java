package com.kydas.build.organizations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import com.kydas.build.users.UserDTO;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class OrganizationDTO extends BaseDTO {
    @NotBlank
    private String name;

    @Nullable
    private List<UserDTO> employees;

    @Nullable
    private String imageId;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant updatedAt;
}
