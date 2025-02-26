package tech.trvihnls.services;

import tech.trvihnls.models.entities.InvalidatedToken;

public interface InvalidatedTokenService {
    boolean isTokenInvalidated(String jwtId); 
    void create(InvalidatedToken invalidatedToken);   
}
