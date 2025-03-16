package tech.trvihnls.mobileapis.user.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.mobileapis.user.services.UserService;
import tech.trvihnls.mobileapis.user.dtos.response.UserInfoResponse;
import tech.trvihnls.mobileapis.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.repositories.UserRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCode;

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

    @PostAuthorize("@userSecurity.isCurrentUser(returnObject.id)")
    @Override
    public UserInfoResponse getUserInfo() {
        Long id = SecurityUtils.getCurrentUserId();
        assert id != null;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED));
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .totalStreakPoints(user.getTotalStreakPoints())
                .totalXPPoints(user.getTotalXPPoints())
                .totalGoPoints(user.getTotalGoPoints())
                .build();
    }

    @Override
    @PreAuthorize("@userSecurity.isCurrentUser(#request.id)")
    public UserInfoResponse updateUserInfo(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED));
        user.setName(request.getName());
        User updatedUser = userRepository.save(user);
        return UserInfoResponse.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .totalStreakPoints(updatedUser.getTotalStreakPoints())
                .totalXPPoints(updatedUser.getTotalXPPoints())
                .totalGoPoints(updatedUser.getTotalGoPoints())
                .build();
    }

    @Override
    @PreAuthorize("@userSecurity.isCurrentUser(#id)")
    public void deleteUserInfo(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED));
        userRepository.delete(user);
    }
}
