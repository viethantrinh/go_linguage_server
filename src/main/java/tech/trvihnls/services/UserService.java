package tech.trvihnls.services;

import java.util.Optional;

import tech.trvihnls.models.entities.User;

public interface UserService {
    Optional<User> findByEmail(String email);
    boolean isUserWithEmailExisted(String email);
    User createSignUpUser(User user);
}
