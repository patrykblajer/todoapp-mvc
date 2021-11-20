package io.github.patrykblajer.todo.user.resetpassword;

import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.user.resetpassword.dtos.UserMailDto;
import io.github.patrykblajer.todo.user.resetpassword.dtos.UserPassTokenDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
public class PasswordResetTokenController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetTokenController(UserService userService, PasswordResetTokenService passwordResetTokenService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        var userMailDto = new UserMailDto();
        model.addAttribute("userMailDto", userMailDto);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute UserMailDto userMailDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) throws MessagingException {
        var email = userMailDto.getEmail();
        if (bindingResult.hasErrors()) {
            return "reset-password";
        } else if (userService.existsUserByEmail(email)) {
            passwordResetTokenService.sendTokenToUserMail(email);
            redirectAttributes.addFlashAttribute("emailSentAlert", "emailSentAlert");
            return "redirect:/reset-password";
        } else {
            model.addAttribute("emailNotSentAlert", "emailNotSentAlert");
            return "reset-password";
        }
    }

    @GetMapping("/reset-password-final")
    public String setNewPassword(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        if (passwordResetTokenService.isTokenValid(token)) {
            var userPassDto = UserPassTokenDto.builder().build();
            model.addAttribute("userPassDto", userPassDto);
            userPassDto.setToken(token);
            return "reset-password-final";
        } else {
            redirectAttributes.addFlashAttribute("tokenNotValidAlert", "tokenNotValidAlert");
            return "redirect:/reset-password";
        }
    }

    @PostMapping("/reset-password-final")
    public String setNewPassword(@Valid @ModelAttribute(name = "userPassDto") UserPassTokenDto userPassTokenDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reset-password-final";
        } else if (!userPassTokenDto.getPassword().equals(userPassTokenDto.getRetypePassword())) {
            model.addAttribute("passwordsNotSameAlert", "passwordsNotSameAlert");
            return "reset-password-final";
        } else if (passwordResetTokenService.isTokenValid(userPassTokenDto.getToken())) {
            passwordResetTokenService.setNewPasswordDeleteToken(userPassTokenDto);
            model.addAttribute("passwordChangedAlert", "passwordChangedAlert");
            return "login";
        } else {
            redirectAttributes.addFlashAttribute("tokenNotValidAlert", "tokenNotValidAlert");
            return "redirect:/reset-password";
        }
    }
}