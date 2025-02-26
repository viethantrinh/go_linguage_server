package tech.trvihnls.services;

import tech.trvihnls.models.entities.User;

public interface JwtService {
    String generateToken(User user);
    boolean verifyToken(String token);
    void invalidateToken(String token);
}
