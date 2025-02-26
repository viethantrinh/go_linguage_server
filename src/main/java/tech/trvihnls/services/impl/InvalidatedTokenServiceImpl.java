package tech.trvihnls.services.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.trvihnls.models.entities.InvalidatedToken;
import tech.trvihnls.repositories.InvalidatedTokenRepository;
import tech.trvihnls.services.InvalidatedTokenService;

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
