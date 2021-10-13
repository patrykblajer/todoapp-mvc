package io.github.patrykblajer.todo.user;

import io.github.patrykblajer.todo.task.Task;
import io.github.patrykblajer.todo.user.role.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "first_name")
    private String firstName;
    @JoinColumn(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private String city;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JoinColumn(name = "registration_date")
    private LocalDate registrationDate;
    private boolean banned;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Task> taskList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<UserRole> roles = new HashSet<>();

    public User(Long id, String firstName, String lastName, String email, String password, String city, LocalDate registrationDate, boolean banned) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.city = city;
        this.registrationDate = registrationDate;
        this.banned = banned;
    }
}