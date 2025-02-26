package tech.trvihnls.services;

import java.util.Optional;

import tech.trvihnls.models.entities.Role;

public interface RoleService {
    Optional<Role> findByName(String name);
}
