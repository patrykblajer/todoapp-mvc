package io.github.patrykblajer.todo.user.resetpassword;

import io.github.patrykblajer.todo.config.MailConfig;
import io.github.patrykblajer.todo.user.User;
import io.github.patrykblajer.todo.user.UserService;
import io.github.patrykblajer.todo.user.resetpassword.dtos.UserPassTokenDto;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;
    private final MailConfig mailService;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository, UserService userService, MailConfig mailService) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Transactional
    public void sendTokenToUserMail(String email) throws MessagingException {
        var user = userService.findUserByEmail(email).orElseThrow();
        deletePasswordResetTokensByUser(user);
        var token = generateToken(user);
        mailService.sendMail(email, "Resetowanie hasła", token.getToken());
    }

    private PasswordResetToken generateToken(User user) {
        var newToken = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString().replace("-", ""))
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30).truncatedTo(ChronoUnit.MINUTES))
                .build();
        passwordResetTokenRepository.save(newToken);
        return newToken;
    }

    public boolean isTokenValid(String token) {
        if (isTokenCorrect(token)) {
            var passwordResetToken = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
            return LocalDateTime.now().isBefore(passwordResetToken.getExpiryDate());
        }
        return false;
    }

    private boolean isTokenCorrect(String token) {
        return passwordResetTokenRepository.existsByToken(token);
    }

    public User getUserByPasswordResetToken(String token) {
        return passwordResetTokenRepository.findPasswordResetTokenByToken(token).getUser();
    }

    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

    @Transactional
    public void deletePasswordResetTokensByUser(User user) {
        passwordResetTokenRepository.deleteAllByUser(user);
    }

    @Transactional
    public void setNewPasswordDeleteToken(UserPassTokenDto userPassDto) {
        var user = getUserByPasswordResetToken(userPassDto.getToken());
        userService.setNewPassword(user, userPassDto.getPassword());
        deletePasswordResetToken((userPassDto.getToken()));
    }
}