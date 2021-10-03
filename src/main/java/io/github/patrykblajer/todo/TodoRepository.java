package io.github.patrykblajer.todo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where task.done= ?1")
    List<Task> getSortedTasks(boolean done, Sort sort);

    //TODO
    @Transactional
    @Modifying
    @Query("update Task set description = ?1, startDate = ?2, category = ?3 where id = ?4")
    void modifyTask(String description, LocalDate startDate, Category category, Long id);
}