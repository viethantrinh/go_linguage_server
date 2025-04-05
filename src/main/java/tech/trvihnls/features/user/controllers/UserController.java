package tech.trvihnls.features.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.user.dtos.request.UserUpdateAdminRequest;
import tech.trvihnls.features.user.dtos.request.UserUpdateRequest;
import tech.trvihnls.features.user.dtos.response.UserDetailResponse;
import tech.trvihnls.features.user.dtos.response.UserInfoResponse;
import tech.trvihnls.features.user.dtos.response.UserUpdateAdminResponse;
import tech.trvihnls.features.user.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo() {
        UserInfoResponse response = userService.getUserInfo();
        return ResponseUtils.success(SuccessCodeEnum.RETRIEVE_USER_INFO_SUCCEEDED, response);
    }

    @PutMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUserInfo(@RequestBody UserUpdateRequest request) {
        UserInfoResponse response = userService.updateUserInfo(request);
        return ResponseUtils.success(SuccessCodeEnum.UPDATE_USER_INFO_SUCCEEDED, response);
    }

    @DeleteMapping("/info/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserInfo(@PathVariable String id) {
        userService.deleteUserInfo(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.DELETE_USER_INFO_SUCCEEDED, null);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<ApiResponse<List<UserDetailResponse>>> listAllUsersDetail() {
        var response = userService.getAllUsersWithDetail();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<ApiResponse<UserUpdateAdminResponse>> updateUserByAdmin(@RequestBody UserUpdateAdminRequest request) {
        var response = userService.updateUser(request);
        return ResponseUtils.success(SuccessCodeEnum.UPDATE_USER_INFO_SUCCEEDED, response);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetailInfoByAdmin(@PathVariable String id) {
        var response = userService.getUserInformationById(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.RETRIEVE_USER_INFO_SUCCEEDED, response);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserByAdmin(@PathVariable String id) {
        userService.deleteUserById(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.DELETE_USER_INFO_SUCCEEDED, null);
    }
}
