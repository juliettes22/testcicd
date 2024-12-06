package com.fullstack.movies.backend.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryDto {

    private String id;

    @Builder.Default
    private List<LibraryMovieDto> movies = new ArrayList<>();
}