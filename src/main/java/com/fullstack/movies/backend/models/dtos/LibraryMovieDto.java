package com.fullstack.movies.backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryMovieDto {

    private MovieDto movie;

    private boolean watched;

    private int rating;
}
