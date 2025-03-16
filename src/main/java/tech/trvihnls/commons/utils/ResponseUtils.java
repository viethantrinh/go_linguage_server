package tech.trvihnls.commons.utils;

import org.springframework.http.ResponseEntity;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.dtos.ApiResponse.ErrorResponse;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;

import java.time.LocalDateTime;

public class ResponseUtils {
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCodeEnum successCodeEnum, T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(successCodeEnum.getCode())
                .message(successCodeEnum.getMessage())
                .timestamp(LocalDateTime.now())
                .errorResponse(null)
                .result(data)
                .build();
        return new ResponseEntity<ApiResponse<T>>(response, successCodeEnum.getStatus());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCodeEnum errorCodeEnum, ErrorResponse errorResponse) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(errorCodeEnum.getCode())
                .message(errorCodeEnum.getMessage())
                .timestamp(LocalDateTime.now())
                .result(null)
                .errorResponse(errorResponse)
                .build();

        return new ResponseEntity<ApiResponse<T>>(response, errorCodeEnum.getStatus());
    }
}
