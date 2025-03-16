package tech.trvihnls.commons.exceptions;

import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum);
    }
}
