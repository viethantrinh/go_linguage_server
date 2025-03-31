package tech.trvihnls.features.user.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Achievement;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.AchievementRepository;
import tech.trvihnls.commons.repositories.UserRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;
import tech.trvihnls.features.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.features.user.dtos.response.UserInfoResponse;
import tech.trvihnls.features.user.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_EXISTED));
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED));
        List<Achievement> achievements = achievementRepository.findAll();
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .totalXPPoints(user.getTotalXPPoints())
                .totalGoPoints(user.getTotalGoPoints())
                .achievements(achievements.stream()
                        .map((a) -> AchievementResponse.builder()
                                .name(a.getName())
                                .description(a.getDescription())
                                .imageUrl(a.getImageUrl())
                                .current(user.getTotalGoPoints())
                                .target(a.getGoRewardCondition())
                                .build()).toList()
                )
                .build();
    }

    @Override
    @PreAuthorize("@userSecurity.isCurrentUser(#request.id)")
    public UserInfoResponse updateUserInfo(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED));
        user.setName(request.getName());
        User updatedUser = userRepository.save(user);
        return UserInfoResponse.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .build();
    }

    @Override
    @PreAuthorize("@userSecurity.isCurrentUser(#id)")
    public void deleteUserInfo(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED));
        userRepository.delete(user);
    }
}
