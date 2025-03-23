package tech.trvihnls.commons.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.dtos.ApiResponse.ErrorResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleGenericException(HttpServletRequest request, Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ErrorCodeEnum.UNCATEGORIZED_ERROR.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ErrorCodeEnum.UNCATEGORIZED_ERROR, errorResponse);
    }

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(HttpServletRequest request, AppException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ex.getErrorCodeEnum(), errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(HttpServletRequest request,
            ResourceNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ex.getErrorCodeEnum(), errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(HttpServletRequest request,
            AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ErrorCodeEnum.UNAUTHORIZED, errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                   MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errorLists = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(errorLists)
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseUtils.error(ErrorCodeEnum.BAD_REQUEST, errorResponse);
    }
}
