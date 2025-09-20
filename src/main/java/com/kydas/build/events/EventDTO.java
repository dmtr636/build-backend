package com.kydas.build.events;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class EventDTO extends BaseDTO {
    @NotBlank
    private UUID userId;

    @NotBlank
    private String action;

    @NotNull
    private String actionType;

    @Nullable
    private String objectName;

    @Nullable
    private String objectId;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;

    private Map<String, Object> info = Map.of();
}

