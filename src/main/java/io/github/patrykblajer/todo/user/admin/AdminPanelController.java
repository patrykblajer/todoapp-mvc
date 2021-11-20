package io.github.patrykblajer.todo.user.admin;

import io.github.patrykblajer.todo.user.dtos.UserDto;
import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.user.authorization.AuthService;
import io.github.patrykblajer.todo.user.role.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private final UserService userService;
    private final AuthService authService;

    public AdminPanelController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String admin(Model model) {
        var users = userService.getUserDtoList();
        model.addAttribute("allUsers", users);
        model.addAttribute("loggedUserId", authService.getLoggedUserDto().getId());
        return "admin";
    }

    @Transactional
    @PostMapping("/change-status/{id}")
    public String changeStatus(@PathVariable Long id, @RequestParam boolean banned) {
        userService.changeUserStatus(id, banned);
        return "redirect:/admin";
    }

    @Transactional
    @PostMapping("/change-role/{id}")
    public String changeRole(@PathVariable Long id, @RequestParam Role role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin";
    }

    @Transactional
    @PostMapping("/edit-account/{id}")
    public String editAccountById(@PathVariable Long id, Model model) {
        var userToEdit = userService.findUserDtoById(id);
        model.addAttribute("userToEdit", userToEdit);
        return "admin-editor";
    }

    @Transactional
    @PostMapping("/edit-account/success")
    public String editAccountSuccess(UserDto userDto) {
        userService.editUser(userDto);
        return "redirect:/admin";
    }
}