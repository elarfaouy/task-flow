package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.dto.StoreTaskDto;
import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.UpdateTaskDto;
import com.youcode.taskflow.mapper.TaskMapper;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    @Override
    public List<TaskDto> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(TaskMapper.INSTANCE::taskToTaskDto).collect(Collectors.toList());
    }

    @Override
    public Optional<TaskDto> findOne(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(TaskMapper.INSTANCE::taskToTaskDto);
    }

    @Override
    public TaskDto save(StoreTaskDto storeTaskDto) {
        try {
            Task task = TaskMapper.INSTANCE.storeTaskDtoToTask(storeTaskDto);
            Task save = taskRepository.save(task);
            return TaskMapper.INSTANCE.taskToTaskDto(save);
        } catch (Exception e) {
            throw new RuntimeException("cannot create task");
        }
    }

    @Override
    public TaskDto update(Long id, UpdateTaskDto updateTaskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        try {
            task.setId(id);
            Task update = taskRepository.save(task);
            return TaskMapper.INSTANCE.taskToTaskDto(update);
        } catch (Exception e) {
            throw new RuntimeException("cannot update task");
        }
    }

    @Override
    public TaskDto delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        try {
            taskRepository.delete(task);
            return TaskMapper.INSTANCE.taskToTaskDto(task);
        } catch (Exception e) {
            throw new RuntimeException("cannot delete task");
        }
    }
}
