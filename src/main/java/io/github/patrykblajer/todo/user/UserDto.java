package io.github.patrykblajer.todo.user;

import io.github.patrykblajer.todo.user.role.Role;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Pattern(regexp = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆŠŽ∂ð ,.'-]*",
            message = "{nameValid}")
    @Size(min = 3, max = 20, message = "{nameLength}")
    private String firstName;
    @Pattern(regexp = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆŠŽ∂ð ,.'-]*",
            message = "{nameInvalid}")
    @Size(min = 3, max = 20, message = "{nameLength}")
    private String lastName;
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})",
            message = "{emailInvalid}")
    @NotEmpty(message = "{notEmpty}")
    private String email;
    @Pattern(regexp = "^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String password;
    private String city;
    private LocalDate registrationDate;
    private boolean banned;
    private Role role;

    public UserDto(Long id, String firstName, String lastName, String email, String city, LocalDate registrationDate, boolean banned) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.registrationDate = registrationDate;
        this.banned = banned;
    }

    public UserDto(Long id, String firstName, String lastName, String email, String password, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.city = city;
    }
}