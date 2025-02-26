package tech.trvihnls.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.trvihnls.exceptions.AppException;
import tech.trvihnls.models.entities.Role;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.services.JwtService;
import tech.trvihnls.utils.AppConstants;
import tech.trvihnls.utils.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long validAccessTokenDuration = 3600;

    private byte[] secretKeyBytes;

    @PostConstruct
    public void init() {
        this.secretKeyBytes = secretKey.getBytes();
        log.info("JWT Service initialized successfully");
    }

    /**
     * Generate token based on user information
     *
     * @param user user which signed in
     * @return token which is signed
     * @throws ApplicationException if token generation fails
     */
    @Override
    public String generateToken(User user) {
        try {
            String subject = String.valueOf(user.getId());
            String issuer = AppConstants.ISSUER;
            String scope = extractUserRoles(user);
            Date issueTime = new Date();
            Date expirationTime = new Date(
                    Instant.now().plus(validAccessTokenDuration, ChronoUnit.SECONDS).toEpochMilli());
            String jwtId = UUID.randomUUID().toString();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(issuer)
                    .issueTime(issueTime)
                    .expirationTime(expirationTime)
                    .jwtID(jwtId)
                    .claim("scope", scope)
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS512).build(), claimsSet);
            JWSSigner signer = new MACSigner(secretKey);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate JWT token", e);
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    /**
     * Extract the user's authorities (roles)
     *
     * @param user user which signed in
     * @return authorities list as a space-delimited string
     */
    private String extractUserRoles(User user) {
        List<Role> roles = user.getRoles();
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));
    }

    @Override
    public boolean verifyToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyToken'");
    }

    @Override
    public void invalidateToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invalidateToken'");
    }

}
