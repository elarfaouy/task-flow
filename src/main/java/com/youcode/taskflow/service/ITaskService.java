package com.youcode.taskflow.service;

import com.youcode.taskflow.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ITaskService {
    List<TaskDto> findAll();

    Optional<TaskDto> findOne(Long id);

    TaskDto save(StoreTaskDto storeTaskDto, UserDto authUser);

    TaskDto update(Long id, UpdateTaskDto updateTaskDto, UserDto authUser);

    TaskDto updateStatus(Long id, updateTaskStatusDto updateTaskStatusDto, UserDto authUser);

    TaskDto delete(Long id);
}
