package tech.trvihnls.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tech.trvihnls.models.dtos.auth.IntrospectTokenRequest;
import tech.trvihnls.models.dtos.auth.IntrospectTokenResponse;
import tech.trvihnls.models.dtos.auth.SignInRequest;
import tech.trvihnls.models.dtos.auth.SignInResponse;
import tech.trvihnls.models.dtos.auth.SignOutRequest;
import tech.trvihnls.models.dtos.auth.SignUpRequest;
import tech.trvihnls.models.dtos.auth.SignUpResponse;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.services.AuthService;
import tech.trvihnls.services.JwtService;
import tech.trvihnls.utils.ResponseUtils;
import tech.trvihnls.utils.SuccessCode;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/sign-in")

    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(request);
        return ResponseUtils.success(SuccessCode.SIGN_IN_SUCCEEDED, response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseUtils.success(SuccessCode.SIGN_UP_SUCCEEDED, response);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<Void>> signOut(@RequestBody SignOutRequest request) {
        authService.signOut(request);
        return ResponseUtils.success(SuccessCode.GENERAL_SUCCESS, null);
    }

    @PostMapping("/introspect-token")
    public ResponseEntity<ApiResponse<IntrospectTokenResponse>> introspectToken(
            @RequestBody IntrospectTokenRequest request) {
        String token = request.getToken();
        boolean valid = jwtService.verifyToken(token);
        return ResponseUtils.success(SuccessCode.GENERAL_SUCCESS,
                IntrospectTokenResponse.builder().valid(valid).build());
    }
}
