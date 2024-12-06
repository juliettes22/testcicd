package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.LibraryMovieDto;
import com.fullstack.movies.backend.models.entities.LibraryMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryMovieConverter implements Converter<LibraryMovie, LibraryMovieDto> {

    private final MovieConverter movieConverter;

    @Override
    public LibraryMovieDto convert(LibraryMovie libraryMovie) {
        return LibraryMovieDto.builder()
                .movie(movieConverter.convert(libraryMovie.getMovie()))
                .watched(libraryMovie.isWatched())
                .rating(libraryMovie.getRating())
                .build();
    }
}
