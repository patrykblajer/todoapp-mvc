package io.github.patrykblajer.todo.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {

    private Long id;
    private Long userId;
    @NotBlank
    private String description;
    private Category category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finalDate;
    private String status;
    private boolean done;

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
}