package com.youcode.taskflow.service;

import com.youcode.taskflow.dto.StoreTaskDto;
import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.UpdateTaskDto;
import com.youcode.taskflow.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ITaskService {
    List<TaskDto> findAll();
    Optional<TaskDto> findOne(Long id);
    TaskDto save(StoreTaskDto storeTaskDto, UserDto authUser);
    TaskDto update(Long id, UpdateTaskDto updateTaskDto);
    TaskDto delete(Long id);
}
