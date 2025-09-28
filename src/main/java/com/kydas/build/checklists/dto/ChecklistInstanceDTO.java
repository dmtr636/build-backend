package com.kydas.build.checklists.dto;

import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.crud.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChecklistInstanceDTO extends BaseDTO {
    private ChecklistFormType type;
    private LocalDate checkDate;
    private String templateTitle;
    private String status;
    private List<ChecklistSectionDTO> sections = new ArrayList<>();
}
