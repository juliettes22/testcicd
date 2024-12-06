package com.fullstack.movies.backend.services;


import com.fullstack.movies.backend.exceptions.NotFoundException;
import com.fullstack.movies.backend.exceptions.errors.MovieErrorCode;
import com.fullstack.movies.backend.exceptions.errors.UserErrorCode;
import com.fullstack.movies.backend.models.constants.MovieGenre;
import com.fullstack.movies.backend.models.converters.CommentConverter;
import com.fullstack.movies.backend.models.converters.MovieConverter;
import com.fullstack.movies.backend.models.dtos.CommentDto;
import com.fullstack.movies.backend.models.dtos.CommentPublishDto;
import com.fullstack.movies.backend.models.dtos.MovieDto;
import com.fullstack.movies.backend.models.entities.Comment;
import com.fullstack.movies.backend.models.entities.Movie;
import com.fullstack.movies.backend.models.entities.User;
import com.fullstack.movies.backend.repositories.CommentRepository;
import com.fullstack.movies.backend.repositories.MovieRepository;
import com.fullstack.movies.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService implements BaseService {

    private final MovieRepository movieRepository;
    private final MovieConverter movieConverter;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Transactional
    public MovieDto registerMovie(MovieDto dto) {
        log.info("Registering movie : {}", dto.getName());

        MovieGenre genre = MovieGenre.getByName(dto.getGenre());

        movieRepository.findByImDbId(dto.getImDbId())
                .ifPresent(movie -> {
                    log.info("Movie already exists : {}", movie.getName());
                    throw new NotFoundException(MovieErrorCode.ALREADY_EXISTS);
                });

        Movie movie = Movie.builder()
                .name(dto.getName())
                .plot(dto.getPlot())
                .year(dto.getYear())
                .genre(genre)
                .imDbId(dto.getImDbId())
                .posterUrl(dto.getPosterUrl())
                .build();

        return movieConverter.convert(movieRepository.save(movie));
    }

    @Transactional
    public MovieDto updateMovie(UUID movieId, MovieDto dto) {
        log.info("Updating movie : {}", movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException(MovieErrorCode.NOT_FOUND));

        movie.setName(dto.getName());
        movie.setGenre(MovieGenre.getByName(dto.getGenre()));
        movie.setPlot(dto.getPlot());
        movie.setYear(dto.getYear());
        movie.setPosterUrl(dto.getPosterUrl());

        return movieConverter.convert(movieRepository.save(movie));
    }

    @Transactional(readOnly = true)
    public MovieDto getMovieById(UUID movieId) {
        log.info("Retrieving movie by id : {}", movieId);

        return movieRepository.findById(movieId)
                .map(movieConverter::convert)
                .orElseThrow(() -> new NotFoundException(MovieErrorCode.NOT_FOUND, movieId));
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getMovies() {
        log.info("Finding all movies");

        List<Movie> movies = movieRepository.findAll();

        return movies.stream().map(movieConverter::convert).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> searchMoviesByName(String name) {
        log.info("Finding movies by name : {}", name);

        Set<Movie> movies = movieRepository.findAllByNameContainingIgnoreCase(name);

        return movies.stream().map(movieConverter::convert).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto publishComment(UUID movieId, CommentPublishDto dto) {
        log.info("Publishing comment for movie : {}", movieId);

        User user = this.getUserOrError(dto.getUserId());

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException(MovieErrorCode.NOT_FOUND));

        Comment comment = Comment.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .movie(movie)
                .user(user)
                .build();

        return commentConverter.convert(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForMovie(UUID movieId) {
        log.info("Retrieving comments of movie : {}", movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException(MovieErrorCode.NOT_FOUND));

        return commentRepository.findAllByMovieIdOrderByCreatedAtAsc(movie.getId())
                .stream()
                .map(commentConverter::convert)
                .toList();
    }

    private User getUserOrError(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
    }
}
