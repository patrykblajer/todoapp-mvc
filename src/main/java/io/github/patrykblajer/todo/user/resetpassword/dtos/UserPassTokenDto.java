package io.github.patrykblajer.todo.user.resetpassword.dtos;

import lombok.*;

import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPassTokenDto {
    private String token;
    @Pattern(regexp = "$|^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String password;
    @Pattern(regexp = "$|^(?=.{8,30})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*!=]).*$", message = "{passwordInvalid}")
    private String retypePassword;
}