package tech.trvihnls.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.trvihnls.exceptions.AppException;
import tech.trvihnls.exceptions.ResourceNotFoundException;
import tech.trvihnls.models.dtos.auth.GoogleAuthRequest;
import tech.trvihnls.models.dtos.auth.GoogleAuthResponse;
import tech.trvihnls.models.dtos.auth.SignInRequest;
import tech.trvihnls.models.dtos.auth.SignInResponse;
import tech.trvihnls.models.dtos.auth.SignOutRequest;
import tech.trvihnls.models.dtos.auth.SignUpRequest;
import tech.trvihnls.models.dtos.auth.SignUpResponse;
import tech.trvihnls.models.entities.InvalidatedToken;
import tech.trvihnls.models.entities.Role;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.services.AuthService;
import tech.trvihnls.services.InvalidatedTokenService;
import tech.trvihnls.services.JwtService;
import tech.trvihnls.services.RoleService;
import tech.trvihnls.services.UserService;
import tech.trvihnls.utils.ErrorCode;
import tech.trvihnls.utils.RoleEnum;

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

        User user = userService.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if (!passwordEncoder.matches(rawPassword, user.getPassword()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (!user.isEnabled())
            throw new AppException(ErrorCode.UNAUTHENTICATED);

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
            throw new AppException(ErrorCode.USER_EXISTED);

        Role role = roleService.findByName(RoleEnum.ROLE_USER.getName())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_EXISTED));

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
                throw new AppException(ErrorCode.UNAUTHENTICATED);
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
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                
                if (!user.isEnabled()) {
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
            } else {
                // User doesn't exist, create new account
                Role role = roleService.findByName(RoleEnum.ROLE_USER.getName())
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_EXISTED));

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
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String generateSecurePassword() {
        return java.util.UUID.randomUUID().toString();
    }
}
