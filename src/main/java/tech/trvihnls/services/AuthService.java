package tech.trvihnls.services;

import tech.trvihnls.models.dtos.auth.*;

public interface AuthService {
    SignInResponse signIn(SignInRequest request);

    SignUpResponse signUp(SignUpRequest request);

    void signOut(SignOutRequest request);

    GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request);
}
