package com.youcode.taskflow.repository;

import com.youcode.taskflow.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.status != 'DONE' and t.dueDate < current_date ")
    List<Task> findOutdatedTasks();

    @Query(value = "select distinct j.user_id\n" +
            "from jeton_usages j\n" +
            "inner join tasks t on t.id = j.task_id\n" +
            "where j.user_id = t.assign_to_id\n" +
            "and extract(DAY FROM now() - j.action_date) > 1", nativeQuery = true)
    List<Long> findUsersWithUnrespondedTaskChangeRequests();
}
