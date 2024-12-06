package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.LibraryDto;
import com.fullstack.movies.backend.models.dtos.LibraryMovieDto;
import com.fullstack.movies.backend.models.entities.Library;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LibraryConverter implements Converter<Library, LibraryDto> {

    private final LibraryMovieConverter libraryMovieConverter;
    @Override
    public LibraryDto convert(Library library) {

        List<LibraryMovieDto> movies = library.getMovies().stream()
                .map(libraryMovieConverter::convert)
                .toList();

        return LibraryDto.builder()
                .id(library.getId().toString())
                .movies(movies)
                .build();
    }
}
