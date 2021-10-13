package io.github.patrykblajer.todo.task;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
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
}