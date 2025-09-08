package com.kydas.build.files;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FileDTO {
    private UUID id;
    private UUID userId;
    private String originalFileName;
    private Long size;
    private String type;

    @JsonFormat(pattern = DateUtils.ISO_DATE_TIME_FORMAT)
    private OffsetDateTime createDate;

    public FileDTO(File file) {
        this.id = file.getId();
        this.userId = file.getUserId();
        this.originalFileName = file.getOriginalFileName();
        this.size = file.getSize();
        this.type = file.getType();
        this.createDate = file.getCreateDate();
    }
}
