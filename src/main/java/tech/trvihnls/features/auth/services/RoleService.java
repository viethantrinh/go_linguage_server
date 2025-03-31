package tech.trvihnls.features.auth.services;

import tech.trvihnls.commons.domains.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
