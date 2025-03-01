package tech.trvihnls.services;

import tech.trvihnls.models.dtos.auth.GoogleAuthRequest;
import tech.trvihnls.models.dtos.auth.GoogleAuthResponse;
import tech.trvihnls.models.dtos.auth.SignInRequest;
import tech.trvihnls.models.dtos.auth.SignInResponse;
import tech.trvihnls.models.dtos.auth.SignOutRequest;
import tech.trvihnls.models.dtos.auth.SignUpRequest;
import tech.trvihnls.models.dtos.auth.SignUpResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest request);

    SignUpResponse signUp(SignUpRequest request);

    void signOut(SignOutRequest request);

    GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request);
}
