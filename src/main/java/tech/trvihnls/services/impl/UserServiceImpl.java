package tech.trvihnls.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.repositories.UserRepository;
import tech.trvihnls.services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isUserWithEmailExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User createSignUpUser(User user) {
        return userRepository.save(user);
    }

   
}
