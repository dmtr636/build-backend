package com.kydas.build.checklists.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kydas.build.checklists.enums.ChecklistFormType;
import com.kydas.build.core.crud.BaseDTO;
import com.kydas.build.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChecklistInstanceDTO extends BaseDTO {
    private ChecklistFormType type;
    @JsonFormat(pattern = DateUtils.ISO_DATE_FORMAT, timezone = "UTC")
    private LocalDate checkDate;
    private String templateTitle;
    private String status;
    private List<ChecklistSectionDTO> sections = new ArrayList<>();
}
