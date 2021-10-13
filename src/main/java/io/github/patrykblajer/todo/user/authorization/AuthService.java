package io.github.patrykblajer.todo.user.authorization;

import io.github.patrykblajer.todo.user.User;
import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private UserService userService;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserDto getLoggedUserDto() {
        var userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserDtoByEmail(userMail);
    }

    public User getLoggedUser() {
        var userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userMail)
                .orElseThrow();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}