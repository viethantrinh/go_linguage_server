package tech.trvihnls.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.models.Role;
import tech.trvihnls.repositories.RoleRepository;
import tech.trvihnls.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> listAll() {
        return roleRepository.getAllByUserId(1L);
    }

}
