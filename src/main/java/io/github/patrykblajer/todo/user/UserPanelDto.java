package io.github.patrykblajer.todo.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPanelDto {
    private Long id;
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})",
            message = "{emailInvalid}")
    @NotEmpty(message = "{notEmpty}")
    private String email;
    @Pattern(regexp = "$|^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String password;
    @Pattern(regexp = "$|^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String retypePassword;
    private String city;
}