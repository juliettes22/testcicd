package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.MovieDto;
import com.fullstack.movies.backend.models.entities.Movie;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter implements Converter<Movie, MovieDto> {

    @Override
    public MovieDto convert(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .name(movie.getName())
                .plot(movie.getPlot())
                .year(movie.getYear())
                .posterUrl(movie.getPosterUrl())
                .genre(movie.getGenre().toString())
                .imDbId(movie.getImDbId())
                .build();
    }
}
