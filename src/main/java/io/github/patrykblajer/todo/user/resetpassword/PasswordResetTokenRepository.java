package io.github.patrykblajer.todo.user.resetpassword;

import io.github.patrykblajer.todo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    boolean existsByToken(String token);

    PasswordResetToken findPasswordResetTokenByToken(String token);

    void deleteByToken(String token);

    void deleteAllByUser(User user);
}