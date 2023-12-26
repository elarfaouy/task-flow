package com.youcode.taskflow.dto;

import com.youcode.taskflow.domain.enums.TaskPriority;
import com.youcode.taskflow.domain.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate assignDate;
    private LocalDate dueDate;
    private UserDto assignTo;
    private UserDto createdBy;
    private List<TagDto> tags;
}
