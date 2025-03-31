package tech.trvihnls.features.auth.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.InvalidatedToken;
import tech.trvihnls.commons.repositories.InvalidatedTokenRepository;
import tech.trvihnls.features.auth.services.InvalidatedTokenService;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public boolean isTokenInvalidated(String jwtId) {
        return invalidatedTokenRepository.existsByJwtId(jwtId);
    }

    @Override
    public void create(InvalidatedToken invalidatedToken) {
        invalidatedTokenRepository.save(invalidatedToken);
    }

}
