package tech.trvihnls.mobileapis.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.mobileapis.user.dtos.response.UserInfoResponse;
import tech.trvihnls.mobileapis.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.mobileapis.user.services.UserService;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCode;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo() {
        UserInfoResponse response = userService.getUserInfo();
        return ResponseUtils.success(SuccessCode.RETRIEVE_USER_INFO_SUCCEEDED, response);
    }

    @PutMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUserInfo(@RequestBody UserUpdateRequest request) {
        UserInfoResponse response = userService.updateUserInfo(request);
        return ResponseUtils.success(SuccessCode.UPDATE_USER_INFO_SUCCEEDED, response);
    }

    @DeleteMapping("/info/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserInfo(@PathVariable String id) {
        userService.deleteUserInfo(Long.parseLong(id));
        return ResponseUtils.success(SuccessCode.DELETE_USER_INFO_SUCCEEDED, null);
    }
}
