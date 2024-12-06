package com.fullstack.movies.backend.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum AuthenticationErrorCode implements ErrorCode {
    JWT_EXPIRED("AUTH_004","JWT token expired"),
    JWT_INVALID("AUTH_003","Invalid JWT token"),
    UNAUTHORIZED("AUTH_002","Unauthorized"),
    FAILED("AUTH_001","Authentication failed");


    private final String code;
    private final String description;
}
