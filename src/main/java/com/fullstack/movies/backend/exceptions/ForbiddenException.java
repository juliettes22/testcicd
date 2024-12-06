package com.fullstack.movies.backend.exceptions;

import com.fullstack.movies.backend.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenException extends ErrorCodeException {

    public ForbiddenException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ForbiddenException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
