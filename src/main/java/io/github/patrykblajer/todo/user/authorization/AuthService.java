package io.github.patrykblajer.todo.user.authorization;

import io.github.patrykblajer.todo.user.User;
import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.user.role.UserRole;
import io.github.patrykblajer.todo.user.role.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private UserService userService;
    private final UserRoleService userRoleService;

    public AuthService(PasswordEncoder passwordEncoder, UserRoleService userRoleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
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

    public void updateAuthToken(User user) {
        List<UserRole> userRoles = userRoleService.findUserRoleByUser(user);
        List<GrantedAuthority> actualAuth = userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRoles.toString()))
                .collect(Collectors.toList());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), actualAuth);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}