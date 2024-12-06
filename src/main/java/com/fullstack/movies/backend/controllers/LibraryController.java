package com.fullstack.movies.backend.controllers;

import com.fullstack.movies.backend.models.dtos.LibraryDto;
import com.fullstack.movies.backend.models.dtos.LibraryMovieDto;
import com.fullstack.movies.backend.models.dtos.MovieStatusDto;
import com.fullstack.movies.backend.models.dtos.RatingDto;
import com.fullstack.movies.backend.services.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/libraries", produces = "application/json")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(summary = "Get library of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Library found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LibraryDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Library not found")
    })
    @GetMapping("/{libraryId}")
    public ResponseEntity<LibraryDto> getMyLibrary(@PathVariable UUID libraryId) {
        return ResponseEntity.ok(libraryService.getCurrentUserLibrary(libraryId));
    }

    @Operation(summary = "Add a movie to a library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movie added to corresponding library",
                    content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Movie or Library not found")
    })
    @PostMapping("/{libraryId}/movies/{movieId}")
    public ResponseEntity<Void> addMovieToLibrary(@PathVariable UUID libraryId, @PathVariable UUID movieId) {
        libraryService.addMovieToLibrary(libraryId, movieId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Rate a movie in a library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated in corresponding library",
                    content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Movie or Library not found")
    })
    @PatchMapping("/{libraryId}/movies/{movieId}/rating")
    public ResponseEntity<LibraryMovieDto> rateMovieInLibrary(
            @PathVariable UUID libraryId,
            @PathVariable UUID movieId,
            @RequestBody @Valid RatingDto rate
            ) {
        return ResponseEntity.ok(libraryService.patchLibraryMovieRating(libraryId, movieId, rate));
    }

    @Operation(summary = "Patch a movie status in a library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated in corresponding library",
                    content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Movie or Library not found")
    })
    @PatchMapping("/{libraryId}/movies/{movieId}/watched")
    public ResponseEntity<LibraryMovieDto> patchStatusMovieInLibrary(
            @PathVariable UUID libraryId,
            @PathVariable UUID movieId,
            @RequestBody @Valid MovieStatusDto dto
    ) {
        return ResponseEntity.ok(libraryService.patchLibraryMovieStatus(libraryId, movieId, dto));
    }

}
