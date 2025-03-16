package tech.trvihnls.commons.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.dtos.ApiResponse.ErrorResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.ErrorCode;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleGenericException(HttpServletRequest request, Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ErrorCode.UNCATEGORIZED_ERROR.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ErrorCode.UNCATEGORIZED_ERROR, errorResponse);
    }

    @ExceptionHandler(exception = AppException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(HttpServletRequest request, AppException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ex.getErrorCode(), errorResponse);
    }

    @ExceptionHandler(exception = ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(HttpServletRequest request,
            ResourceNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ex.getErrorCode(), errorResponse);
    }

    @ExceptionHandler(exception = AuthorizationDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(HttpServletRequest request,
            AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ErrorCode.UNAUTHORIZED, errorResponse);
    }
}
