package tech.trvihnls.features.user.services;

import tech.trvihnls.commons.domains.User;
import tech.trvihnls.features.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.features.user.dtos.response.UserInfoResponse;

import java.util.Optional;

public interface UserService {


    Optional<User> findByEmail(String email);
    User findById(Long id);
    User createOrUpdate(User user);
    boolean isUserWithEmailExisted(String email);
    User createSignUpUser(User user);

    UserInfoResponse getUserInfo();
    UserInfoResponse updateUserInfo(UserUpdateRequest request);
    void deleteUserInfo(long id);
}
