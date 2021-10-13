package io.github.patrykblajer.todo.user.authorization;

import io.github.patrykblajer.todo.user.UserDto;
import io.github.patrykblajer.todo.user.UserPanelDto;
import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.weatherwidget.WeatherService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final WeatherService weatherService;

    public AuthController(UserService userService, AuthService authService, WeatherService weatherService) {
        this.userService = userService;
        this.authService = authService;
        this.weatherService = weatherService;
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

    @GetMapping("/editaccount")
    public String editAccount(Model model) {
        var userDto = authService.getLoggedUserDto();
        var userPanelDto = new UserPanelDto(userDto.getId(), userDto.getEmail(), userDto.getPassword(), userDto.getCity());
        model.addAttribute("userPanelDto", userPanelDto);
        return "editaccount";
    }

    @PostMapping("/editaccount")
    public String editAccountSuccess(@Valid @ModelAttribute(name = "userPanelDto") UserPanelDto userPanelDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userPanelDto", userPanelDto);
            return "editaccount";
        }
        try {
            userService.editUserMailPass(userPanelDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("emailExistAlert", "emailExistAlert");
            return "editaccount";
        }
        return "redirect:/logout";
    }
}