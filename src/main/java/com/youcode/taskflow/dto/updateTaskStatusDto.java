package com.youcode.taskflow.dto;

import com.youcode.taskflow.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class updateTaskStatusDto implements Serializable {
    @NotNull(message = "status cannot be null")
    private TaskStatus status;
}
