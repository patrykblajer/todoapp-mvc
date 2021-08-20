package io.github.patrykblajer.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepo extends JpaRepository<Task, Long> {


    @Query("select task from Task task where task.finalDate is null order by task.startDate asc, task.id desc")
    List<Task> findNotDoneOrderByDateAsc();

    @Query("select task from Task task where task.finalDate is not null order by task.finalDate desc, task.startDate desc")
    List<Task> findDoneOrderByDateDesc();

    @Transactional
    @Modifying
    @Query("update Task set finalDate = ?1 where id = ?2")
    void setFinalDate(LocalDate date, Long id);

    //TODO
    @Transactional
    @Modifying
    @Query("update Task set description = ?1, startDate = ?2, category = ?3 where id = ?4")
    void modifyTask(String description, LocalDate startDate, Category category, Long id);
}