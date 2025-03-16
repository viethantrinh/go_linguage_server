package tech.trvihnls.commons.exceptions;

import lombok.Getter;
import tech.trvihnls.commons.utils.enums.ErrorCode;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
