package com.kydas.build.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class NotificationDTO extends BaseDTO {
    private UUID projectId;
    private String projectName;
    private NotificationType type;
    private UUID objectId;
    private String content;
    private UUID authorId;
    private boolean read;
    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT, timezone = "UTC")
    private Instant createdAt;
}
