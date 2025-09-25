package com.kydas.build.projects.dto;

import com.kydas.build.core.crud.BaseCommentDTO;
import com.kydas.build.files.FileDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProjectWorkCommentDTO extends BaseCommentDTO {
    private UUID workId;
    private List<FileDTO> files = new ArrayList<>();
}
