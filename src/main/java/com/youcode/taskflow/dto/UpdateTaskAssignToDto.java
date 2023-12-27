package com.youcode.taskflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateTaskAssignToDto implements Serializable {
    @NotNull(message = "user assignTo cannot be null")
    private UserDto assignTo;
}
