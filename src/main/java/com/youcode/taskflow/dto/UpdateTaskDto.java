package com.youcode.taskflow.dto;

import com.youcode.taskflow.domain.enums.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateTaskDto implements Serializable {
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "description cannot be blank")
    private String description;
    @NotNull(message = "priority cannot be null")
    private TaskPriority priority;
    @FutureOrPresent(message = "assign date cannot be in past")
    private LocalDate assignDate;
    @FutureOrPresent(message = "due date cannot be in past")
    private LocalDate dueDate;
    @NotNull(message = "tags cannot be null")
    @Size(message = "tags cannot be less than 2", min = 2)
    private List<TagDto> tags;
}
