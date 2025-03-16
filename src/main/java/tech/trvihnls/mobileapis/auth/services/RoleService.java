package tech.trvihnls.mobileapis.auth.services;

import tech.trvihnls.commons.domains.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
