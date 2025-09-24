package com.kydas.build.dictionaries.work.stage;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConstructionWorkStageDTO {
    private UUID id;
    private UUID workId;
    private Integer stageNumber;
    private String stageName;
}