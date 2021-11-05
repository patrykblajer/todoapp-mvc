package io.github.patrykblajer.todo.user;

import io.github.patrykblajer.todo.user.role.Role;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}