package com.fullstack.movies.backend.exceptions.errors;

public interface ErrorCode {
    String getCode();
    String getDescription();

    default String printableError() {
        return String.join(" - ", getCode(), getDescription());
    }
}
