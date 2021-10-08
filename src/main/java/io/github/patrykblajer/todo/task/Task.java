package io.github.patrykblajer.todo.task;

import io.github.patrykblajer.todo.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "final_date")
    private LocalDate finalDate;
    @Transient
    private String status;
    private boolean done;

    public Task(Long id, User user, String description, Category category, LocalDate startDate) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
    }
}