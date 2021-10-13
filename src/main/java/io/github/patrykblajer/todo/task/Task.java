package io.github.patrykblajer.todo.task;

import io.github.patrykblajer.todo.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
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
}