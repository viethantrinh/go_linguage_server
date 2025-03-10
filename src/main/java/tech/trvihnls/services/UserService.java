package tech.trvihnls.services;

import tech.trvihnls.models.dtos.user.UserInfoResponse;
import tech.trvihnls.models.dtos.user.UserUpdateRequest;
import tech.trvihnls.models.entities.User;

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
