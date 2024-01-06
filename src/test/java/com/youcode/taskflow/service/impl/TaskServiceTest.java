package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.domain.entity.User;
import com.youcode.taskflow.domain.enums.TaskStatus;
import com.youcode.taskflow.dto.*;
import com.youcode.taskflow.mapper.TaskMapper;
import com.youcode.taskflow.mapper.UserMapper;
import com.youcode.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;

    @Test
    public void test_findAll_returnsListOfAllTasks() {
        // Arrange
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<TaskDto> result = taskService.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void test_findOne_returnsOptionalOfTaskDtoWithGivenIdIfExists() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        Optional<TaskDto> result = taskService.findOne(taskId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
    }

    // save creates a new task with default status "TODO", sets the creator to the authenticated user, and saves it to the database
//    @Test
//    public void test_save_createsNewTaskWithDefaultStatusAndSavesToDatabase() {
//        // Arrange
//        StoreTaskDto storeTaskDto = new StoreTaskDto();
//        UserDto authUser = new UserDto();
//        Task task = new Task();
//        when(TaskMapper.INSTANCE.storeTaskDtoToTask(storeTaskDto)).thenReturn(task);
//        when(UserMapper.INSTANCE.userDtoToUser(authUser)).thenReturn(new User());
//        when(taskRepository.save(task)).thenReturn(task);
//
//        // Act
//        TaskDto result = taskService.save(storeTaskDto, authUser);
//
//        // Assert
//        assertEquals(TaskStatus.TODO, task.getStatus());
//        assertNotNull(task.getCreatedBy());
//        verify(taskRepository).save(task);
//    }
//
//    // save throws a RuntimeException if the authenticated user is a regular user and the task is assigned to someone else
//    @Test
//    public void test_save_throwsRuntimeExceptionIfUserIsRegularAndTaskIsAssignedToSomeoneElse() {
//        // Arrange
//        StoreTaskDto storeTaskDto = new StoreTaskDto();
//        UserDto authUser = new UserDto();
//        RoleDto role = new RoleDto();
//        role.setName("user");
//        authUser.setRole(role);
//        Task task = new Task();
//        task.setAssignTo(new User());
//        when(TaskMapper.INSTANCE.storeTaskDtoToTask(storeTaskDto)).thenReturn(task);
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> taskService.save(storeTaskDto, authUser));
//    }

    @Test
    public void test_save_throwsRuntimeExceptionIfDurationBetweenAssignDateAndDueDateIsMoreThanThreeDays() {
        // Arrange
        StoreTaskDto storeTaskDto = new StoreTaskDto();
        storeTaskDto.setAssignDate(LocalDate.now());
        storeTaskDto.setDueDate(LocalDate.now().plusDays(4));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.save(storeTaskDto, new UserDto()));
    }

    @Test
    public void test_update_throwsRuntimeExceptionIfUserIsRegularAndDidNotCreateTheTask() {
        // Arrange
        Long taskId = 1L;
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        UserDto authUser = new UserDto();
        RoleDto role = new RoleDto();
        role.setName("user");
        authUser.setRole(role);
        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.update(taskId, updateTaskDto, authUser));
    }
}