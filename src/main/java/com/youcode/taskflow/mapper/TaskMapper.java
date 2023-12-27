package com.youcode.taskflow.mapper;

import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.dto.StoreTaskDto;
import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.UpdateTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    Task storeTaskDtoToTask(StoreTaskDto storeTaskDto);
    Task updateTaskDtoToTask(UpdateTaskDto updateTaskDto);
    TaskDto taskToTaskDto(Task task);
}
