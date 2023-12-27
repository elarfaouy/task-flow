package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.Tag;
import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.domain.enums.TaskStatus;
import com.youcode.taskflow.dto.*;
import com.youcode.taskflow.mapper.TaskMapper;
import com.youcode.taskflow.mapper.UserMapper;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;

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
    public TaskDto save(StoreTaskDto storeTaskDto, UserDto authUser) {
        // convert DTO to task entity and set default values:
        Task task = TaskMapper.INSTANCE.storeTaskDtoToTask(storeTaskDto);
        task.setStatus(TaskStatus.TODO); // set default status to "TODO".
        task.setCreatedBy(UserMapper.INSTANCE.userDtoToUser(authUser)); // set creator to authenticate user.

        if (authUser.getRole().getName().equals("user") && !Objects.equals(authUser.getId(), task.getAssignTo().getId())) {
            throw new RuntimeException("as user you can not assign task to others");
        }

        if (isDurationMoreThanThreeDays(task.getAssignDate(), task.getDueDate())) {
            throw new RuntimeException("invalid duration or more than 3 days");
        }

        // retrieve and validate tags:
        List<Tag> tags = storeTaskDto.getTags()
                .stream()
                .map(tagDto -> tagRepository.findById(tagDto.getId()).orElseThrow(() -> new RuntimeException("tag not found: " + tagDto.getId())))
                .toList();
        task.setTags(tags);

        Task save = taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDto(save);
    }

    @Override
    public TaskDto update(Long id, UpdateTaskDto updateTaskDto, UserDto authUser) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        if (authUser.getRole().getName().equals("user") && !Objects.equals(task.getCreatedBy().getId(), authUser.getId())) {
            throw new RuntimeException("you cannot update this task, you dont have the right permission");
        }

        Task newTask = TaskMapper.INSTANCE.updateTaskDtoToTask(updateTaskDto);
        newTask.setId(id);
        newTask.setStatus(task.getStatus());
        newTask.setCreatedBy(task.getCreatedBy());
//todo        newTask.setTags(task.getTags());

        Task update = taskRepository.save(newTask);
        return TaskMapper.INSTANCE.taskToTaskDto(update);
    }

    @Override
    public TaskDto updateStatus(Long id, updateTaskStatusDto updateTaskStatusDto, UserDto authUser) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        if (!Objects.equals(task.getAssignTo().getId(), authUser.getId())) {
            throw new RuntimeException("you cannot update status of this task, you dont have the right permission");
        }

        if (task.getStatus() != updateTaskStatusDto.getStatus() && updateTaskStatusDto.getStatus().equals(TaskStatus.DONE) && task.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("you cannot set status to done after due date");
        }

        task.setStatus(updateTaskStatusDto.getStatus());

        Task update = taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDto(update);
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

    private Boolean isDurationMoreThanThreeDays(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate) || startDate.plusDays(3).isBefore(endDate);
    }
}
