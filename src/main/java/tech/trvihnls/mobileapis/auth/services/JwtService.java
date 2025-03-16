package tech.trvihnls.mobileapis.auth.services;

import tech.trvihnls.commons.domains.User;

public interface JwtService {
    String generateToken(User user);

    boolean verifyToken(String token);

    boolean invalidateToken(String token);
}
