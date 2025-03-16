package tech.trvihnls.mobileapis.auth.services;

import tech.trvihnls.mobileapis.auth.dtos.request.GoogleAuthRequest;
import tech.trvihnls.mobileapis.auth.dtos.request.SignInRequest;
import tech.trvihnls.mobileapis.auth.dtos.request.SignOutRequest;
import tech.trvihnls.mobileapis.auth.dtos.request.SignUpRequest;
import tech.trvihnls.mobileapis.auth.dtos.response.GoogleAuthResponse;
import tech.trvihnls.mobileapis.auth.dtos.response.SignInResponse;
import tech.trvihnls.mobileapis.auth.dtos.response.SignUpResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest request);

    SignUpResponse signUp(SignUpRequest request);

    void signOut(SignOutRequest request);

    GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request);
}
