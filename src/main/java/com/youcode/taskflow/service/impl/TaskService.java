package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.JetonUsage;
import com.youcode.taskflow.domain.entity.Tag;
import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.domain.entity.User;
import com.youcode.taskflow.domain.enums.JetonUsageAction;
import com.youcode.taskflow.domain.enums.TaskStatus;
import com.youcode.taskflow.dto.*;
import com.youcode.taskflow.mapper.TaskMapper;
import com.youcode.taskflow.mapper.UserMapper;
import com.youcode.taskflow.repository.JetonUsageRepository;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final JetonUsageRepository jetonUsageRepository;
    private final UserRepository userRepository;

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

        if (isDurationMoreThanThreeDays(updateTaskDto.getAssignDate(), updateTaskDto.getDueDate())) {
            throw new RuntimeException("invalid duration or more than 3 days");
        }

        Task newTask = TaskMapper.INSTANCE.updateTaskDtoToTask(updateTaskDto);
        newTask.setId(id);
        newTask.setStatus(task.getStatus());
        newTask.setAssignTo(task.getAssignTo());
        newTask.setCreatedBy(task.getCreatedBy());
        newTask.setJetonUsage(task.getJetonUsage());

        // retrieve and validate tags:
        List<Tag> tags = updateTaskDto.getTags()
                .stream()
                .map(tagDto -> tagRepository.findById(tagDto.getId()).orElseThrow(() -> new RuntimeException("tag not found: " + tagDto.getId())))
                .toList();
        task.setTags(tags);
        newTask.setTags(tags);

        Task update = taskRepository.save(newTask);
        return TaskMapper.INSTANCE.taskToTaskDto(update);
    }

    @Override
    public TaskDto updateAssignTo(Long id, UpdateTaskAssignToDto updateTaskAssignToDto, UserDto authUser) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));
        User assignTo = userRepository.findById(updateTaskAssignToDto.getAssignTo().getId()).orElseThrow(() -> new RuntimeException("user assign to not found"));

        if (!Objects.equals(authUser.getRole().getName(), "admin")) {
            if (!Objects.equals(task.getAssignTo().getId(), authUser.getId())) {
                throw new RuntimeException("you cannot update a task not assign to you");
            }

            if (task.getJetonUsage() != null) {
                throw new RuntimeException("this task cannot be updated, replaced task");
            }

            User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("user not found"));

            if (user.getJetons() == 0) {
                throw new RuntimeException("you dont have any jeton to make this action");
            }

            JetonUsage jetonUsage = new JetonUsage();
            jetonUsage.setAction(JetonUsageAction.UPDATE);

            return performUsageJeton(task, user, jetonUsage);
        }

        task.setAssignTo(assignTo);
        taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDto(task);
    }

    @Override
    public TaskDto updateStatus(Long id, updateTaskStatusDto updateTaskStatusDto, UserDto authUser) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        if (!Objects.equals(task.getAssignTo().getId(), authUser.getId())) {
            throw new RuntimeException("you cannot update status of this task, you dont have the right permission");
        }

        if (updateTaskStatusDto.getStatus().equals(TaskStatus.OUTDATED)) {
            throw new RuntimeException("you cannot set status to outdated");
        }

        if (task.getStatus() != updateTaskStatusDto.getStatus() && updateTaskStatusDto.getStatus().equals(TaskStatus.DONE) && task.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("you cannot set status to done after due date");
        }

        task.setStatus(updateTaskStatusDto.getStatus());

        Task update = taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDto(update);
    }

    @Override
    public TaskDto delete(Long id, UserDto authUser) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));

        if (!Objects.equals(task.getCreatedBy().getId(), authUser.getId()) && !Objects.equals(authUser.getRole().getName(), "admin")) {
            if (!Objects.equals(task.getAssignTo().getId(), authUser.getId())) {
                throw new RuntimeException("you cannot delete a task not assign to you");
            }

            if (task.getJetonUsage() != null) {
                throw new RuntimeException("this task cannot be deleted, replaced task");
            }

            User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("user not found"));

            if (user.getJetons() == 0) {
                throw new RuntimeException("you dont have any jeton to make this action");
            }

            JetonUsage lastDeletedJetonUsage = user.getJetonUsages().stream()
                    .filter(jetonUsage -> jetonUsage.getAction().equals(JetonUsageAction.DELETE))
                    .max(Comparator.comparing(JetonUsage::getActionDate))
                    .orElse(null);

            if (lastDeletedJetonUsage != null && lastDeletedJetonUsage.getActionDate().plusDays(30).isAfter(LocalDate.now())) {
                throw new RuntimeException("you cannot use jeton to delete, last action of delete is under 30 days");
            }

            JetonUsage jetonUsage = new JetonUsage();
            jetonUsage.setAction(JetonUsageAction.DELETE);

            return performUsageJeton(task, user, jetonUsage);
        }

        taskRepository.delete(task);
        return TaskMapper.INSTANCE.taskToTaskDto(task);
    }

    private TaskDto performUsageJeton(Task task, User user, JetonUsage jetonUsage) {
        jetonUsage.setActionDate(LocalDate.now());
        jetonUsage.setUser(user);
        jetonUsage.setTask(task);

        jetonUsageRepository.save(jetonUsage);

        user.setJetons(user.getJetons() - 1);
        userRepository.save(user);

        return TaskMapper.INSTANCE.taskToTaskDto(task);
    }

    private Boolean isDurationMoreThanThreeDays(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate) || startDate.plusDays(3).isBefore(endDate);
    }
}
