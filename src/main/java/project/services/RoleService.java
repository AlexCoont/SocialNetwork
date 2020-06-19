package project.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.handlerExceptions.EntityNotFoundException;
import project.models.Role;
import project.models.enums.RoleEnum;
import project.repositories.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @SneakyThrows(EntityNotFoundException.class)
    public Role find(RoleEnum role) {
        return roleRepository.findByName(role).orElseThrow(
            () -> new EntityNotFoundException("User role not found")
        );
    }
}