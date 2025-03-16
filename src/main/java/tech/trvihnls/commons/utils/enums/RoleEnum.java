package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ROLE_ADMIN("ADMIN", "Admin can control anything in the system"),
    ROLE_USER("USER", "User can only access to the learning material");

    private final String name;
    private final String description;
}
