package com.fullstack.movies.backend.exceptions;

import com.fullstack.movies.backend.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends ErrorCodeException {

    public ConflictException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}