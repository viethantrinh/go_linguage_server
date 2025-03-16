package tech.trvihnls.mobileapis.user.services;

import tech.trvihnls.mobileapis.user.dtos.response.UserInfoResponse;
import tech.trvihnls.mobileapis.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.commons.domains.User;

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
