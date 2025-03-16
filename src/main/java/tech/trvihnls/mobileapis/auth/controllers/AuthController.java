package tech.trvihnls.mobileapis.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.mobileapis.auth.dtos.request.*;
import tech.trvihnls.mobileapis.auth.dtos.response.GoogleAuthResponse;
import tech.trvihnls.mobileapis.auth.dtos.response.IntrospectTokenResponse;
import tech.trvihnls.mobileapis.auth.dtos.response.SignInResponse;
import tech.trvihnls.mobileapis.auth.dtos.response.SignUpResponse;
import tech.trvihnls.mobileapis.auth.services.AuthService;
import tech.trvihnls.mobileapis.auth.services.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(request);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_IN_SUCCEEDED, response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_UP_SUCCEEDED, response);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<Void>> signOut(@RequestBody SignOutRequest request) {
        authService.signOut(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PostMapping("/introspect-token")
    public ResponseEntity<ApiResponse<IntrospectTokenResponse>> introspectToken(
            @RequestBody IntrospectTokenRequest request) {
        String token = request.getToken();
        boolean valid = jwtService.verifyToken(token);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS,
                IntrospectTokenResponse.builder().valid(valid).build());
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<GoogleAuthResponse>> googleSignIn(@RequestBody GoogleAuthRequest request) {
        GoogleAuthResponse response = authService.authenticateWithGoogle(request);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_IN_SUCCEEDED, response);
    }
}
