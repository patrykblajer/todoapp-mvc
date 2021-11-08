package io.github.patrykblajer.todo.user.role;

import io.github.patrykblajer.todo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT role from UserRole where user.id = ?1")
    Role getRoleByUserId(Long id);

    List<UserRole> findUserRoleByUser(User user);
}