package com.youcode.taskflow.dto;

import com.youcode.taskflow.domain.enums.TaskPriority;
import com.youcode.taskflow.domain.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class StoreTaskDto implements Serializable {
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "description cannot be blank")
    private String description;
    @NotNull(message = "priority cannot be null")
    private TaskPriority priority;
    @NotNull(message = "status cannot be null")
    private TaskStatus status;
    @FutureOrPresent(message = "assign date cannot be in past")
    private LocalDate assignDate;
    @FutureOrPresent(message = "due date cannot be in past")
    private LocalDate dueDate;
    @NotNull(message = "user assignTo cannot be null")
    private UserDto assignTo;
    @NotNull(message = "user createdBy cannot be null")
    private UserDto createdBy;
}
