package com.fullstack.movies.backend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "library_movies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryMovie extends AuditDateEntity {

    @Embeddable
    public record LibraryMovieKeyId(UUID libraryId, UUID movieId) {}

    @EmbeddedId
    private LibraryMovieKeyId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("libraryId")
    @JoinColumn(name = "library_id", referencedColumnName = "id")
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Column(name = "watched", columnDefinition = "BOOLEAN", nullable = false)
    private boolean watched;

    @Column(name = "rating", columnDefinition = "INTEGER")
    private int rating;

}
