package tech.trvihnls.utils;

import org.springframework.http.ResponseEntity;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.models.dtos.base.ApiResponse.ErrorResponse;
import tech.trvihnls.utils.enums.ErrorCode;
import tech.trvihnls.utils.enums.SuccessCode;

import java.time.LocalDateTime;

public class ResponseUtils {
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .timestamp(LocalDateTime.now())
                .errorResponse(null)
                .result(data)
                .build();
        return new ResponseEntity<ApiResponse<T>>(response, successCode.getStatus());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode, ErrorResponse errorResponse) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .result(null)
                .errorResponse(errorResponse)
                .build();

        return new ResponseEntity<ApiResponse<T>>(response, errorCode.getStatus());
    }
}
