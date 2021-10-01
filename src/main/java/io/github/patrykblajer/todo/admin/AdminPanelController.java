package io.github.patrykblajer.todo.admin;

import io.github.patrykblajer.todo.authorization.AuthService;
import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserService;
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

    @GetMapping("")
    public String admin(Model model) {
        var users = userService.getUserDtoList();
        model.addAttribute("allUsers", users);
        model.addAttribute("loggedUserId", authService.getLoggedUserDto().getId());
        return "admin";
    }

    @Transactional
    @GetMapping("/changestatus/{id}")
    public String changeStatus(@PathVariable Long id, @RequestParam boolean banned) {
        userService.changeUserStatus(id, banned);
        return "redirect:/admin";
    }

    @Transactional
    @GetMapping("/changerole/{id}")
    public String changeRole(@PathVariable Long id, @RequestParam Role role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin";
    }

    @Transactional
    @GetMapping("/editaccount/{id}")
    public String editAccountById(@PathVariable Long id, Model model) {
        var userToEdit = userService.findUserDtoById(id);
        model.addAttribute("userToEdit", userToEdit);
        return "admineditor";
    }

    @Transactional
    @PostMapping("/editaccount/success")
    public String editAccountSuccess(UserDto userDto) {
        userService.editUser(userDto);
        return "redirect:/admin";
    }
}