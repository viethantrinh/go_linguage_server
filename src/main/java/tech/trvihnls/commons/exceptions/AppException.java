package tech.trvihnls.commons.exceptions;

import lombok.Getter;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCodeEnum errorCodeEnum;

    public AppException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }
}
