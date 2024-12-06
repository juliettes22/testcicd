package com.fullstack.movies.backend.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum LibraryErrorCode implements ErrorCode {

    ALREADY_ADDED("LIBRARY_004", "Movie already added to library"),
    USER_ALREADY_HAS_LIBRARY("LIBRARY_003", "User already has library"),
    USER_DOES_NOT_OWN_LIBRARY("LIBRARY_002", "User does not own library"),
    NOT_FOUND("LIBRARY_001", "Library not found");


    private final String code;
    private final String description;
}
