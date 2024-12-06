package com.fullstack.movies.backend.services;

import com.fullstack.movies.backend.exceptions.ConflictException;
import com.fullstack.movies.backend.exceptions.NotFoundException;
import com.fullstack.movies.backend.exceptions.UnauthorizedException;
import com.fullstack.movies.backend.exceptions.errors.AuthenticationErrorCode;
import com.fullstack.movies.backend.exceptions.errors.LibraryErrorCode;
import com.fullstack.movies.backend.exceptions.errors.MovieErrorCode;
import com.fullstack.movies.backend.models.constants.MovieGenre;
import com.fullstack.movies.backend.models.converters.LibraryConverter;
import com.fullstack.movies.backend.models.converters.LibraryMovieConverter;
import com.fullstack.movies.backend.models.dtos.*;
import com.fullstack.movies.backend.models.entities.Library;
import com.fullstack.movies.backend.models.entities.LibraryMovie;
import com.fullstack.movies.backend.models.entities.Movie;
import com.fullstack.movies.backend.repositories.LibraryMovieRepository;
import com.fullstack.movies.backend.repositories.LibraryRepository;
import com.fullstack.movies.backend.repositories.MovieRepository;
import com.fullstack.movies.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService implements BaseService {

    private final MovieRepository movieRepository;
    private final LibraryRepository libraryRepository;
    private final LibraryMovieRepository libraryMovieRepository;
    private final LibraryConverter libraryConverter;
    private final LibraryMovieConverter libraryMovieConverter;

    @Transactional
    public void addMovieToLibrary(UUID libraryId, UUID movieId) {
        log.info("Adding movie to library : {}", movieId);

        libraryMovieRepository.findById(new LibraryMovie.LibraryMovieKeyId(libraryId, movieId))
                .ifPresent(libraryMovie -> {
                    throw new ConflictException(LibraryErrorCode.ALREADY_ADDED);
                });

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException(MovieErrorCode.NOT_FOUND, movieId));

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND, libraryId));

        LibraryMovie libraryMovie = LibraryMovie.builder()
                .movie(movie)
                .library(library)
                .build();

        libraryMovieRepository.save(libraryMovie);
    }

    @Transactional
    public void addMovieToLibrary(UUID libraryId, MovieDto dto) {
        log.info("Adding movie to library : {}", dto.getName());
        Optional<Movie> mayBeMovie;

        if (Objects.isNull(dto.getId())) {
            MovieGenre genre = MovieGenre.getByName(dto.getGenre());
            Movie movie = Movie.builder()
                    .name(dto.getName())
                    .plot(dto.getPlot())
                    .year(dto.getYear())
                    .genre(genre)
                    .imDbId(dto.getImDbId())
                    .build();
            mayBeMovie = Optional.of(movieRepository.save(movie));
        } else {
            mayBeMovie = movieRepository.findById(dto.getId());
        }

        if (mayBeMovie.isEmpty()) {
            throw new NotFoundException(MovieErrorCode.NOT_FOUND, dto.getId());
        }

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND, libraryId));

        LibraryMovie libraryMovie = LibraryMovie.builder()
                .movie(mayBeMovie.get())
                .library(library)
                .build();

        libraryMovieRepository.save(libraryMovie);

    }

    @Transactional
    public LibraryMovieDto patchLibraryMovieStatus(UUID libraryId, UUID movieId, MovieStatusDto dto) {
        log.info("Updating movie status in library : {}", movieId);

        LibraryMovie libraryMovie = libraryMovieRepository.findById(new LibraryMovie.LibraryMovieKeyId(libraryId, movieId))
                .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND, movieId));

        return getCurrentUser()
                .map(user -> {
                    if (!user.getId().equals(libraryMovie.getLibrary().getUser().getId())) {
                        throw new UnauthorizedException(LibraryErrorCode.USER_DOES_NOT_OWN_LIBRARY);
                    }
                    libraryMovie.setWatched(dto.isWatched());
                    return libraryMovieConverter.convert(libraryMovieRepository.save(libraryMovie));
                })
                .orElseThrow(() -> new UnauthorizedException(AuthenticationErrorCode.UNAUTHORIZED));
    }

    @Transactional
    public LibraryMovieDto patchLibraryMovieRating(UUID libraryId, UUID movieId, RatingDto dto) {
        log.info("Updating movie rating in library : {}", movieId);

        LibraryMovie libraryMovie = libraryMovieRepository.findById(new LibraryMovie.LibraryMovieKeyId(libraryId, movieId))
                .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND, movieId));

        return getCurrentUser()
                .map(user -> {
                    if (!user.getId().equals(libraryMovie.getLibrary().getUser().getId())) {
                        throw new UnauthorizedException(LibraryErrorCode.USER_DOES_NOT_OWN_LIBRARY);
                    }
                    libraryMovie.setRating(dto.getRating());
                    return libraryMovieConverter.convert(libraryMovieRepository.save(libraryMovie));
                })
                .orElseThrow(() -> new UnauthorizedException(AuthenticationErrorCode.UNAUTHORIZED));
    }

    @Transactional
    public boolean removeMovieFromLibrary(UUID libraryId, UUID movieId) {
        log.info("Removing movie from library : {}", movieId);

        libraryMovieRepository.findById(new LibraryMovie.LibraryMovieKeyId(libraryId, movieId))
                .ifPresent(libraryMovieRepository::delete);

        return true;
    }

    @Transactional(readOnly = true)
    public LibraryDto getLibraryById(UUID libraryId) {
        log.info("Retrieving library by id : {}", libraryId);
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND, libraryId));

        return libraryConverter.convert(library);
    }

    @Transactional(readOnly = true)
    public LibraryDto getCurrentUserLibrary(UUID libraryId) {
        log.info("Retrieving library of current user");
        return getCurrentUser()
                .map(user -> libraryRepository.findByUserId(user.getId())
                    .filter(library -> library.getId().equals(libraryId))
                    .map(libraryConverter::convert)
                    .orElseThrow(() -> new NotFoundException(LibraryErrorCode.NOT_FOUND)))
                .orElseThrow(() -> new UnauthorizedException(AuthenticationErrorCode.UNAUTHORIZED));
    }

}
