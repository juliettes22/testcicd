package com.fullstack.movies.backend.models.entities;

import com.fullstack.movies.backend.models.constants.MovieGenre;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "movies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Movie extends AuditDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "imdb_id", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    private String imDbId;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "poster_url", columnDefinition = "VARCHAR(1000)")
    private String posterUrl;

    @Column(name = "plot", columnDefinition = "VARCHAR(1000)")
    private String plot;

    @Column(name = "genre", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private MovieGenre genre;

    @Column(name = "year", columnDefinition = "INTEGER")
    private Integer year;

}
