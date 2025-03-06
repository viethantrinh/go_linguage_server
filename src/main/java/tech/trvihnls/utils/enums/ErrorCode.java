package tech.trvihnls.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_ERROR(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),
    TOKEN_GENERATION_FAILED(5001, HttpStatus.UNAUTHORIZED, "Failed to generate token"),
    UNAUTHENTICATED(5002, HttpStatus.UNAUTHORIZED, "User not authenticated"),
    USER_EXISTED(5003, HttpStatus.BAD_REQUEST, "Error caused by user existed"),
    ROLE_NOT_EXISTED(5004, HttpStatus.NOT_FOUND, "Error caused by role not exsited in system"),
    UNAUTHORIZED(5005, HttpStatus.UNAUTHORIZED, "User not have permisson to access"),
    GOOGLE_AUTH_FAILED(5006, HttpStatus.UNAUTHORIZED, "Google authentication failed"),
    INVALID_GOOGLE_TOKEN(5007, HttpStatus.BAD_REQUEST, "Invalid Google token"), 
    USER_NOT_EXISTED(5008, HttpStatus.NOT_FOUND, "Error caused by user not existed"),
    SUBSCRIPTION_NOT_EXISTED(5009, HttpStatus.NOT_FOUND, "Error caused by subscription plan not existed"),
    PAYMENT_ERROR(5010, HttpStatus.INTERNAL_SERVER_ERROR, "Error caused by stripe exception");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
