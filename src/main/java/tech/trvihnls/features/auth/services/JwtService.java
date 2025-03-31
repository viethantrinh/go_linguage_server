package tech.trvihnls.features.auth.services;

import tech.trvihnls.commons.domains.User;

public interface JwtService {
    String generateToken(User user);

    boolean verifyToken(String token);

    boolean invalidateToken(String token);
}
