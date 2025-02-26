package tech.trvihnls.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityUtils {

    /**
     * Get the current authentication object
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * Get the current user ID
     */
    public static String getCurrentUserId() {
        Authentication auth = getAuthentication();
        return auth != null ? auth.getName() : null;
    }
    
    /**
     * Get the current user's authorities/roles
     */
    public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication auth = getAuthentication();
        return auth != null ? auth.getAuthorities() : Collections.emptyList();
    }
    
    /**
     * Get the current user's roles as strings (without the "ROLE_" prefix)
     */
    public static Set<String> getCurrentUserRoles() {
        return getCurrentUserAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.toSet());
    }
    
    /**
     * Check if the current user has a specific role
     */
    public static boolean hasRole(String role) {
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return getCurrentUserAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(roleWithPrefix));
    }

}
