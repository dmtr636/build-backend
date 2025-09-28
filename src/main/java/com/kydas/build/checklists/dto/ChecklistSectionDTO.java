package com.kydas.build.checklists.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChecklistSectionDTO {
    private String title;
    private int orderIndex;
    private List<ChecklistItemAnswerDTO> items = new ArrayList<>();
}

