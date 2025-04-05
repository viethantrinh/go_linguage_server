package tech.trvihnls.features.user.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Achievement;
import tech.trvihnls.commons.domains.Role;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.AchievementRepository;
import tech.trvihnls.commons.repositories.RoleRepository;
import tech.trvihnls.commons.repositories.UserRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;
import tech.trvihnls.features.user.dtos.request.UserUpdateAdminRequest;
import tech.trvihnls.features.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.features.user.dtos.response.RoleResponse;
import tech.trvihnls.features.user.dtos.response.UserDetailResponse;
import tech.trvihnls.features.user.dtos.response.UserInfoResponse;
import tech.trvihnls.features.user.dtos.response.UserUpdateAdminResponse;
import tech.trvihnls.features.user.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final RoleRepository roleRepository;

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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserDetailResponse> getAllUsersWithDetail() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToUserDto).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserUpdateAdminResponse updateUser(UserUpdateAdminRequest request) {
        Optional<User> byId = userRepository.findById(request.getId());

        if (byId.isEmpty()) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED);
        }

        User user = byId.get();

        user.setName(request.getName());
        user.setEnabled(request.isEnabled());
        user.getRoles().clear();
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new AppException(ErrorCodeEnum.ROLE_NOT_EXISTED));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return UserUpdateAdminResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .enabled(savedUser.isEnabled())
                .roles(
                        savedUser.getRoles().stream().map(
                                r -> RoleResponse.builder()
                                        .name(r.getName())
                                        .description(r.getDescription())
                                        .build()
                        ).toList())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserDetailResponse getUserInformationById(Long id) {
        Optional<User> byId = userRepository.findById(id);

        if (byId.isEmpty()) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED);
        }
        User user = byId.get();

        return mapUserToUserDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    protected UserDetailResponse mapUserToUserDto(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(
                        user.getRoles().stream().map(
                                r -> RoleResponse.builder()
                                        .name(r.getName())
                                        .description(r.getDescription())
                                        .build()
                        ).toList()
                )
                .build();
    }
}
