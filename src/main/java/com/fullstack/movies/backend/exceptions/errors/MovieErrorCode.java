package com.fullstack.movies.backend.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum MovieErrorCode implements ErrorCode {

    ALREADY_EXISTS("MOVIE_002", "Movie already exists"),
    NOT_FOUND("MOVIE_001", "Movie not found");

    private final String code;
    private final String description;
}
