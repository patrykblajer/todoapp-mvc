package io.github.patrykblajer.todo.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.patrykblajer.todo.user.User;
import io.github.patrykblajer.todo.user.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findUserByEmail(email);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            if (!user.isBanned()) {
                Set<SimpleGrantedAuthority> roles = user.getRoles().stream()
                        .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                        .collect(Collectors.toSet());
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
            }
        }
        throw new UsernameNotFoundException("USER_NOT_FOUND_OR_IS_BANNED");
    }
}