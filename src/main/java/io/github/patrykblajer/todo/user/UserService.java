package io.github.patrykblajer.todo.user;

import io.github.patrykblajer.todo.user.authorization.AuthService;
import io.github.patrykblajer.todo.user.role.Role;
import io.github.patrykblajer.todo.user.role.UserRole;
import io.github.patrykblajer.todo.user.role.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final AuthService authService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService(UserRoleService userRoleService, AuthService authService) {
        this.userRoleService = userRoleService;
        this.authService = authService;
    }

    public void saveWithDefaultRole(UserDto userDto) {
        var newUser = new User(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
                userDto.getPassword(), LocalDate.now(), false);
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
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()))
                .orElseThrow();
    }

    public UserDto findUserDtoByEmail(String userMail) {
        return userRepository.findUserByEmail(userMail)
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
                        user.getEmail(), user.getPassword(), user.getRegistrationDate(), user.isBanned(),
                        userRoleService.getUserRoleByUserId(user.getId())))
                .orElseThrow();
    }

    public Optional<User> findUserByEmail(String userMail) {
        return userRepository.findUserByEmail(userMail);
    }

    public List<UserDto> getUserDtoList() {
        var users = userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                        user.getRegistrationDate(), user.isBanned()))
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
        user.setPassword(authService.encodePassword(userDto.getPassword()));
    }

    @Transactional
    public void editUserMailPass(UserPanelDto userPanelDto) {
        var user = userRepository.getById(userPanelDto.getId());
        user.setEmail(userPanelDto.getEmail());
        user.setPassword(authService.encodePassword(userPanelDto.getPassword()));
    }
}