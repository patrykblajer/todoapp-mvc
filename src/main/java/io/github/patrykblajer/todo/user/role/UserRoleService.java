package io.github.patrykblajer.todo.user.role;

import org.springframework.stereotype.Service;

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
}