package io.github.patrykblajer.todo.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserPanelDto {
    private Long id;
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})",
            message = "{emailInvalid}")
    @NotEmpty(message = "{notEmpty}")
    private String email;
    private String city;
    @Pattern(regexp = "^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String password;

    public UserPanelDto(Long id, String email, String password, String city) {
        this.id = id;
        this.email = email;
        this.city = city;
        this.password = password;
    }
}