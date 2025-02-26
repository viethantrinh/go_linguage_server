package tech.trvihnls.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.trvihnls.models.entities.Role;
import tech.trvihnls.repositories.RoleRepository;
import tech.trvihnls.services.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    
    
}
