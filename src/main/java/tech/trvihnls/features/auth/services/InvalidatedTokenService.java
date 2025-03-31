package tech.trvihnls.features.auth.services;

import tech.trvihnls.commons.domains.InvalidatedToken;

public interface InvalidatedTokenService {
    boolean isTokenInvalidated(String jwtId); 
    void create(InvalidatedToken invalidatedToken);   
}
