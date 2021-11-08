package io.github.patrykblajer.todo.user;

import io.github.patrykblajer.todo.user.authorization.AuthService;
import io.github.patrykblajer.todo.user.role.Role;
import io.github.patrykblajer.todo.user.role.UserRole;
import io.github.patrykblajer.todo.user.role.UserRoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveWithDefaultRole(UserDto userDto) {
        var newUser = User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .city(userDto.getCity())
                .registrationDate(LocalDate.now())
                .banned(false)
                .build();
        String passwordHash = authService.encodePassword(newUser.getPassword());
        newUser.setPassword(passwordHash);
        List<UserRole> list = Collections.singletonList(new UserRole(newUser, Role.ROLE_USER));
        newUser.setRoles(new HashSet<>(list));
        userRepository.save(newUser);
    }

    public User getUserById(Long userId) {
        return userRepository.getById(userId);
    }

    public UserDto findUserDtoById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .city(user.getCity())
                        .build())
                .orElseThrow();
    }

    public UserDto findUserDtoByEmail(String userMail) {
        return userRepository.findUserByEmail(userMail)
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .city(user.getCity())
                        .registrationDate(user.getRegistrationDate())
                        .banned(user.isBanned())
                        .role(userRoleService.getUserRoleByUserId(user.getId()))
                        .build())
                .orElseThrow();
    }

    public Optional<User> findUserByEmail(String userMail) {
        return userRepository.findUserByEmail(userMail);
    }

    public List<UserDto> getUserDtoList() {
        var users = userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .registrationDate(user.getRegistrationDate())
                        .banned(user.isBanned())
                        .build())
                .collect(Collectors.toList());
        for (UserDto user : users) {
            user.setRole(userRoleService.getUserRoleByUserId(user.getId()));
        }
        return users;
    }

    public void changeUserStatus(Long id, boolean banned) {
        var user = userRepository.getById(id);
        user.setBanned(banned);
    }

    public void changeUserRole(Long id, Role role) {
        var user = userRepository.getById(id);
        var roles = user.getRoles().stream()
                .map(userRole -> new UserRole(userRole.getId(), user, role))
                .collect(Collectors.toSet());
        userRoleService.saveAll(roles);
    }

    public void editUser(UserDto userDto) {
        var user = getUserById(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    @Transactional
    public void editUserMailPass(UserPanelDto userPanelDto) {
        var user = userRepository.getById(userPanelDto.getId());
        user.setEmail(userPanelDto.getEmail());
        user.setCity(userPanelDto.getCity());
        if (!userPanelDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userPanelDto.getPassword()));
        }
        authService.updateAuthToken(user);
    }
}