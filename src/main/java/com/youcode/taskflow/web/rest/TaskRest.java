package com.youcode.taskflow.web.rest;

import com.youcode.taskflow.dto.StoreTaskDto;
import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.UpdateTaskDto;
import com.youcode.taskflow.dto.UserDto;
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
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody @Valid UpdateTaskDto updateTaskDto){
        TaskDto taskDto = taskService.update(id, updateTaskDto);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id){
        TaskDto taskDto = taskService.delete(id);
        return ResponseEntity.ok(taskDto);
    }
}
