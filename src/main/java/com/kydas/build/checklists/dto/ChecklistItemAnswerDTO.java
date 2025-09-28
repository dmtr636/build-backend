package com.kydas.build.checklists.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kydas.build.checklists.enums.AnswerStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChecklistItemAnswerDTO {
    private UUID templateItemId;
    private String itemNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String text;
    private AnswerStatus answer;
}
