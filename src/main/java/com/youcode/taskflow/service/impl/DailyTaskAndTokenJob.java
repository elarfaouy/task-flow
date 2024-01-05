package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.Task;
import com.youcode.taskflow.domain.entity.User;
import com.youcode.taskflow.domain.enums.TaskStatus;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

@RequiredArgsConstructor
public class DailyTaskAndTokenJob implements Job {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Task> updateTaskList = taskRepository
                .findOutdatedTasks()
                .stream()
                .peek(task -> task.setStatus(TaskStatus.OUTDATED))
                .toList();
        taskRepository.saveAll(updateTaskList);

        List<Long> usersWithUnrespondedTaskChangeRequests = taskRepository.findUsersWithUnrespondedTaskChangeRequests();

        List<User> updatedUsers = userRepository
                .findAll()
                .stream()
                .peek(user -> {
                    user.setJetons(2);
                    if (usersWithUnrespondedTaskChangeRequests.contains(user.getId())) {
                        user.setJetons(4);
                    }
                })
                .toList();
        userRepository.saveAll(updatedUsers);
    }
}
