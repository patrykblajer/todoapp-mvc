package io.github.patrykblajer.todo.authorization;

import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.userpanel.UserPanelDto;
import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.userpanel.UserPanelService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final UserPanelService userPanelService;

    public AuthController(UserService userService, AuthService authService, UserPanelService userPanelService) {
        this.userService = userService;
        this.authService = authService;
        this.userPanelService = userPanelService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        boolean showLoginErrorMessage = error != null;
        model.addAttribute("showLoginErrorMessage", showLoginErrorMessage);
        model.addAttribute("loginErrorAlert", "loginErrorAlert");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        var newUserDto = new UserDto();
        model.addAttribute("newUserDto", newUserDto);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid UserDto userDto, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.saveWithDefaultRole(userDto);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("registerErrorAlert", "registerErrorAlert");
            return "redirect:/register";
        }
        model.addAttribute("registerSuccessAlert", "registerSuccessAlert");
        return "login";
    }


    @GetMapping("/editaccount")
    public String editAccount(Model model) {
        var userDto = authService.getLoggedUserDto();
        var userPanelDto = userPanelService.mapToUserPanelDto(userDto);
        model.addAttribute("userPanelDto", userPanelDto);
        return "editaccount";
    }

    @Transactional
    @PostMapping("/editaccount")
    public String editAccountSuccess(UserPanelDto userPanelDto) {
        userPanelService.editUserMailPass(userPanelDto);
        return "redirect:/logout";
    }
}