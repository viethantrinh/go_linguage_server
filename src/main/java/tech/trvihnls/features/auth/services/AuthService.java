package tech.trvihnls.features.auth.services;

import tech.trvihnls.features.auth.dtos.request.GoogleAuthRequest;
import tech.trvihnls.features.auth.dtos.request.SignInRequest;
import tech.trvihnls.features.auth.dtos.request.SignOutRequest;
import tech.trvihnls.features.auth.dtos.request.SignUpRequest;
import tech.trvihnls.features.auth.dtos.response.GoogleAuthResponse;
import tech.trvihnls.features.auth.dtos.response.SignInResponse;
import tech.trvihnls.features.auth.dtos.response.SignUpResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest request);

    SignUpResponse signUp(SignUpRequest request);

    void signOut(SignOutRequest request);

    GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request);
}
