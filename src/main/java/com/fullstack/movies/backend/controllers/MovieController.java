package com.fullstack.movies.backend.controllers;

import com.fullstack.movies.backend.models.dtos.CommentDto;
import com.fullstack.movies.backend.models.dtos.CommentPublishDto;
import com.fullstack.movies.backend.models.dtos.MovieDto;
import com.fullstack.movies.backend.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/movies", produces = "application/json")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "Get a movie by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @GetMapping(path = "/{movieId}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movies found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDto.class)) })
    })
    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies() {
        return ResponseEntity.ok(movieService.getMovies());
    }

    @Operation(summary = "Search movies by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movies found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDto.class)) })
    })
    @GetMapping(path = "/search")
    public ResponseEntity<List<MovieDto>> searchMovieByName(@RequestParam String name) {
        return ResponseEntity.ok(movieService.searchMoviesByName(name));
    }

    @Operation(summary = "Update a movie by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(
            @RequestBody @Valid MovieDto dto,
            @PathVariable @Valid UUID movieId
            ) {
        return ResponseEntity.ok(movieService.updateMovie(movieId, dto));
    }

    @Operation(summary = "Register a new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<MovieDto> registerMovie(@RequestBody @Valid MovieDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.registerMovie(dto));
    }

    @Operation(summary = "Publish a comment for a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment published",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(path = "/{movieId}/comments")
    public ResponseEntity<CommentDto> publishCommentMovie(
            @PathVariable @Valid UUID movieId,
            @RequestBody @Valid CommentPublishDto dto
            ) {
        return ResponseEntity.ok(movieService.publishComment(movieId, dto));
    }

    @Operation(summary = "Get all comments for a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @GetMapping(path = "/{movieId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsForMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(movieService.getCommentsForMovie(movieId));
    }
}
