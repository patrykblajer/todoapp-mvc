package io.github.patrykblajer.todo.task;

import io.github.patrykblajer.todo.task.category.Category;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TaskDto {

    private Long id;
    private Long userId;
    private String description;
    private Category category;
    private LocalDate startDate;
    private LocalDate finalDate;
    private String status;
    private boolean done;

    public TaskDto() {
    }

    public TaskDto(Long id, String description, Category category, LocalDate startDate, String status) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.status = status;
    }

    public TaskDto(Long id, String description, Category category, LocalDate startDate, LocalDate finalDate) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.finalDate = finalDate;
    }

    public TaskDto(Long userId, LocalDate startDate, boolean done) {
        this.userId = userId;
        this.startDate = startDate;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getStartDate() {
        return startDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}