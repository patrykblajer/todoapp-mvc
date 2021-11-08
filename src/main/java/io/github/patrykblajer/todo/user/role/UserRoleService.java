package io.github.patrykblajer.todo.user.role;

import io.github.patrykblajer.todo.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public Role getUserRoleByUserId(Long id) {
        return userRoleRepository.getRoleByUserId(id);
    }

    public void saveAll(Set<UserRole> roles) {
        userRoleRepository.saveAll(roles);
    }

    public List<UserRole> findUserRoleByUser(User user) {
        return userRoleRepository.findUserRoleByUser(user);
    }
}