package tech.trvihnls.services.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.trvihnls.exceptions.AppException;
import tech.trvihnls.exceptions.ResourceNotFoundException;
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

}
