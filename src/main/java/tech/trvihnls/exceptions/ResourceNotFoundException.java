package tech.trvihnls.exceptions;

import tech.trvihnls.utils.ErrorCode;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
