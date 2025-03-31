package tech.trvihnls.features.auth.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.InvalidatedToken;
import tech.trvihnls.commons.domains.Role;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.RoleEnum;
import tech.trvihnls.features.auth.dtos.request.GoogleAuthRequest;
import tech.trvihnls.features.auth.dtos.request.SignInRequest;
import tech.trvihnls.features.auth.dtos.request.SignOutRequest;
import tech.trvihnls.features.auth.dtos.request.SignUpRequest;
import tech.trvihnls.features.auth.dtos.response.GoogleAuthResponse;
import tech.trvihnls.features.auth.dtos.response.SignInResponse;
import tech.trvihnls.features.auth.dtos.response.SignUpResponse;
import tech.trvihnls.features.auth.services.AuthService;
import tech.trvihnls.features.auth.services.InvalidatedTokenService;
import tech.trvihnls.features.auth.services.JwtService;
import tech.trvihnls.features.auth.services.RoleService;
import tech.trvihnls.features.user.services.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final InvalidatedTokenService invalidatedTokenService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public SignInResponse signIn(SignInRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        User user = userService.findByEmail(email).orElseThrow(() -> new AppException(ErrorCodeEnum.UNAUTHENTICATED));

        if (!passwordEncoder.matches(rawPassword, user.getPassword()))
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);

        if (!user.isEnabled())
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);

        String token = jwtService.generateToken(user);

        return SignInResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();

        if (userService.isUserWithEmailExisted(email))
            throw new AppException(ErrorCodeEnum.USER_EXISTED);

        Role role = roleService.findByName(RoleEnum.ROLE_USER.getName())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.ROLE_NOT_EXISTED));

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .roles(List.of(role))
                .build();

        User signUpUser = userService.createSignUpUser(user);
        String token = jwtService.generateToken(signUpUser);

        return SignUpResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void signOut(SignOutRequest request) {
        String token = request.getToken();

        try {

            if (!jwtService.verifyToken(token)) {
                return;
            }

            // Parse the JWT to extract necessary information
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // Extract token identifier and expiration time
            String jwtId = claimsSet.getJWTID();
            Date expirationTime = claimsSet.getExpirationTime();

            // Add token to invalidated tokens repository
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .jwtId(jwtId)
                    .expirationTime(expirationTime)
                    .build();

            invalidatedTokenService.create(invalidatedToken);
            log.info("User signed out successfully, token invalidated: {}", jwtId);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request) {
        try {
            // Verify the Google ID token
            GoogleIdToken idToken = googleIdTokenVerifier.verify(request.getIdToken());

            if (idToken == null) {
                throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);
            }

            // Extract user info from token
            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user;
            // Check if user already exists
            if (userService.isUserWithEmailExisted(email)) {
                // User exists, retrieve their details
                user = userService.findByEmail(email)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));
                
                if (!user.isEnabled()) {
                    throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);
                }
            } else {
                // User doesn't exist, create new account
                Role role = roleService.findByName(RoleEnum.ROLE_USER.getName())
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.ROLE_NOT_EXISTED));

                // Generate a random secure password for Google users
                String randomPassword = generateSecurePassword();

                user = User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(randomPassword))
                        .enabled(true)
                        .roles(List.of(role))
                        .build();

                user = userService.createSignUpUser(user);
                log.info("Created new user account via Google authentication: {}", email);
            }

            // Generate JWT token for the user
            String token = jwtService.generateToken(user);

            return GoogleAuthResponse.builder()
                    .token(token)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error verifying Google token", e);
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);
        }
    }

    private String generateSecurePassword() {
        return java.util.UUID.randomUUID().toString();
    }
}
