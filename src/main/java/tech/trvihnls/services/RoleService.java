package tech.trvihnls.services;

import tech.trvihnls.models.entities.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
