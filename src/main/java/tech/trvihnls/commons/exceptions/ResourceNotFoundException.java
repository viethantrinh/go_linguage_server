package tech.trvihnls.commons.exceptions;

import tech.trvihnls.commons.utils.enums.ErrorCode;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
