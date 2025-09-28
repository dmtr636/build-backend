package com.kydas.build.checklists.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChecklistSubmitDTO {
    private UUID checklistInstanceId;
    private String status;
    private List<ChecklistItemAnswerDTO> answers = new ArrayList<>();
}
