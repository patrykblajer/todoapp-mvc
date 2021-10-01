package io.github.patrykblajer.todo.userpanel;

import io.github.patrykblajer.todo.authorization.AuthService;
import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPanelService {

    private final AuthService authService;
    private final UserRepository userRepository;

    public UserPanelService(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    public UserPanelDto mapToUserPanelDto(UserDto userDto) {
        return new UserPanelDto(userDto.getId(), userDto.getEmail(), userDto.getPassword());
    }

    public void editUserMailPass(UserPanelDto userPanelDto) {
        var user = userRepository.getById(userPanelDto.getId());
        user.setEmail(userPanelDto.getEmail());
        user.setPassword(authService.encodePassword(userPanelDto.getPassword()));
    }
}