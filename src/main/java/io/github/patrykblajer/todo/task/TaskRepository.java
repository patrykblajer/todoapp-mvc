package io.github.patrykblajer.todo.task;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where task.done= ?1 and task.user.id = ?2")
    List<Task> getSortedTasks(boolean done, Long userId, Sort sort);
}