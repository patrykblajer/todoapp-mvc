package io.github.patrykblajer.todo.user.authorization;

import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.user.dtos.UserDto;
import io.github.patrykblajer.todo.user.dtos.UserPanelDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

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
    public String register(@Valid @ModelAttribute(name = "newUserDto") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newUserDto", userDto);
            return "register";
        } else if (!userDto.getPassword().equals(userDto.getRetypePassword())) {
            model.addAttribute("passwordsNotSameAlert", "passwordsNotSameAlert");
            return "register";
        }
        try {
            userService.saveWithDefaultRole(userDto);
            model.addAttribute("registerSuccessAlert", "registerSuccessAlert");
            return "login";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("emailExistAlert", "emailExistAlert");
            return "register";
        }
    }

    @GetMapping("/edit-account")
    public String editAccount(Model model) {
        var userDto = authService.getLoggedUserDto();
        var userPanelDto = UserPanelDto.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .city(userDto.getCity())
                .build();
        model.addAttribute("userPanelDto", userPanelDto);
        return "edit-account";
    }

    @PostMapping("/edit-account")
    public String editAccountSuccess(@Valid @ModelAttribute(name = "userPanelDto") UserPanelDto userPanelDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userPanelDto", userPanelDto);
            return "edit-account";
        } else if (!userPanelDto.getPassword().equals(userPanelDto.getRetypePassword())) {
            model.addAttribute("passwordsNotSameAlert", "passwordsNotSameAlert");
            return "edit-account";
        }
        try {
            userService.editUserMailPass(userPanelDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("emailExistAlert", "emailExistAlert");
            return "edit-account";
        }
        return "redirect:/";
    }
}