package com.fullstack.movies.backend.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
