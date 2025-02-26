package tech.trvihnls.utils;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    GENERAL_SUCCESS(1000, HttpStatus.OK, "success"), 
    SIGN_IN_SUCCEEDED(1001, HttpStatus.OK, "sign in succeed"),
    SIGN_UP_SUCCEEDED(1001, HttpStatus.OK, "sign up succeed");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
