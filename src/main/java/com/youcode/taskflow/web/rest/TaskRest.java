package com.youcode.taskflow.web.rest;

import com.youcode.taskflow.dto.*;
import com.youcode.taskflow.service.ITaskService;
import com.youcode.taskflow.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskRest {
    private final ITaskService taskService;
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasksDto = taskService.findAll();
        return ResponseEntity.ok(tasksDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getOneTask(@PathVariable Long id) {
        Optional<TaskDto> optionalTaskDto = taskService.findOne(id);
        return optionalTaskDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<TaskDto> storeTask(@RequestParam Long authUserId, @RequestBody @Valid StoreTaskDto storeTaskDto) {
        UserDto authUser = userService.findOne(authUserId);
        TaskDto taskDto = taskService.save(storeTaskDto, authUser);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@RequestParam Long authUserId, @PathVariable Long id, @RequestBody @Valid UpdateTaskDto updateTaskDto) {
        UserDto authUser = userService.findOne(authUserId);
        TaskDto taskDto = taskService.update(id, updateTaskDto, authUser);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskDto> updateTaskAssignTo(@RequestParam Long authUserId, @PathVariable Long id, @RequestBody @Valid UpdateTaskAssignToDto updateTaskAssignToDto) {
        UserDto authUser = userService.findOne(authUserId);
        TaskDto taskDto = taskService.updateAssignTo(id, updateTaskAssignToDto, authUser);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@RequestParam Long authUserId, @PathVariable Long id, @RequestBody @Valid updateTaskStatusDto updateTaskStatusDto) {
        UserDto authUser = userService.findOne(authUserId);
        TaskDto taskDto = taskService.updateStatus(id, updateTaskStatusDto, authUser);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTask(@RequestParam Long authUserId, @PathVariable Long id) {
        UserDto authUser = userService.findOne(authUserId);
        TaskDto taskDto = taskService.delete(id, authUser);
        return ResponseEntity.ok(taskDto);
    }
}
