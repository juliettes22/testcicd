package com.fullstack.movies.backend.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum UserErrorCode implements ErrorCode {

    EMAIL_ALREADY_EXISTS("USER_002", "User with same email already exists"),
    NOT_FOUND("USER_001", "User not found");

    private final String code;

    private final String description;
}
