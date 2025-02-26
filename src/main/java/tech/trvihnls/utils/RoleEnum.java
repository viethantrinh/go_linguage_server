package tech.trvihnls.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String name;
}