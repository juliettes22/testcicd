package com.fullstack.movies.backend.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private UUID id;

    @NotEmpty
    private String name;

    private String posterUrl;

    private String plot;

    @NotEmpty
    private String genre;

    @NotEmpty
    private String imDbId;

    private int year;

}
