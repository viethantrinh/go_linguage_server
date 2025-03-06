package tech.trvihnls.services.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.trvihnls.exceptions.AppException;
import tech.trvihnls.models.entities.InvalidatedToken;
import tech.trvihnls.models.entities.Role;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.services.InvalidatedTokenService;
import tech.trvihnls.services.JwtService;
import tech.trvihnls.utils.AppConstants;
import tech.trvihnls.utils.enums.ErrorCode;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long validAccessTokenDuration = 100000;

    private byte[] secretKeyBytes;

    private final InvalidatedTokenService invalidatedTokenService;

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

    /**
     * Verify the token
     *
     * @param token the string token
     * @return true if the token is valid, otherwise return false
     */
    @Override
    public boolean verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            log.debug("Token is null or empty");
            return false;
        }

        try {
            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify the signature
            JWSVerifier verifier = new MACVerifier(secretKeyBytes);
            if (!signedJWT.verify(verifier)) {
                log.debug("JWT signature verification failed");
                return false;
            }

            // Get JWT claims
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String jwtId = claimsSet.getJWTID();

            // Check if token is in blacklist
            if (invalidatedTokenService.isTokenInvalidated(jwtId)) {
                log.debug("Token is in the invalidated tokens list: {}", jwtId);
                return false;
            }

            // Check expiration
            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime == null || expirationTime.before(new Date())) {
                log.debug("Token is expired");
                return false;
            }

            return true;

        } catch (ParseException e) {
            log.warn("Failed to parse JWT token", e);
            return false;
        } catch (JOSEException e) {
            log.warn("Failed to verify JWT token signature", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during token verification", e);
            return false;
        }
    }

    /**
     * Invalidate a token (add to blacklist)
     *
     * @param token the token to invalidate
     * @return true if successful, false otherwise
     */
    @Override
    public boolean invalidateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            String jwtId = claimsSet.getJWTID();
            Date expirationTime = claimsSet.getExpirationTime();

            // Save to invalidated tokens repository
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .jwtId(jwtId)
                    .expirationTime(expirationTime)
                    .build();

            invalidatedTokenService.create(invalidatedToken);
            log.debug("Token invalidated successfully: {}", jwtId);

            return true;
        } catch (ParseException e) {
            log.error("Failed to parse token during invalidation", e);
            return false;
        } catch (Exception e) {
            log.error("Failed to invalidate token", e);
            return false;
        }
    }

}
