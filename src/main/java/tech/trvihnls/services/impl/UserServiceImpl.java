package tech.trvihnls.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.exceptions.ResourceNotFoundException;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.repositories.UserRepository;
import tech.trvihnls.services.UserService;
import tech.trvihnls.utils.enums.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_EXISTED));
    }

    @Override
    public User createOrUpdate(User user) {
        return userRepository.save(user);
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
