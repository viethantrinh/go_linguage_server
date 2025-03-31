package tech.trvihnls.features.auth.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.trvihnls.commons.utils.SecurityUtils;

@Slf4j
@Component("userSecurity")
public class UserSecurityExpression {

    public boolean isCurrentUser(Long userId) {
        // Implement your logic to check if the current user matches the given userId
        // This is just a placeholder implementation
        long currentUserId = Long.parseLong(SecurityUtils.getAuthentication().getName());
        return currentUserId == userId;
    }
}
