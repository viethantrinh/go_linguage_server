package tech.trvihnls.mobileapis.auth.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.mobileapis.auth.services.RoleService;
import tech.trvihnls.commons.domains.Role;
import tech.trvihnls.commons.repositories.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    
    
}
