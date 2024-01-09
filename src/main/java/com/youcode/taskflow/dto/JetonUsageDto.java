package com.youcode.taskflow.dto;

import com.youcode.taskflow.domain.enums.JetonUsageAction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class JetonUsageDto implements Serializable {
    private Long id;
    private JetonUsageAction action;
    private LocalDate actionDate;
    private UserDto user;
}
