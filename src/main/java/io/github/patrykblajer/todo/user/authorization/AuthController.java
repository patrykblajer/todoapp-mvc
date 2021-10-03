package io.github.patrykblajer.todo.user.authorization;

import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserPanelDto;
import io.github.patrykblajer.todo.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

@Controller
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
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
    public String register(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.saveWithDefaultRole(userDto);
            model.addAttribute("registerSuccessAlert", "registerSuccessAlert");
            return "login";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("registerErrorAlert", "registerErrorAlert");
            return "redirect:/register";
        }
    }


    @GetMapping("/editaccount")
    public String editAccount(Model model) {
        var userDto = authService.getLoggedUserDto();
        var userPanelDto = new UserPanelDto(userDto.getId(), userDto.getEmail(), userDto.getPassword());
        model.addAttribute("userPanelDto", userPanelDto);
        return "editaccount";
    }

    @Transactional
    @PostMapping("/editaccount")
    public String editAccountSuccess(UserPanelDto userPanelDto) {
        userService.editUserMailPass(userPanelDto);
        return "redirect:/logout";
    }
}