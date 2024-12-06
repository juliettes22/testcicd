package com.fullstack.movies.backend.repositories;

import com.fullstack.movies.backend.models.constants.MovieGenre;
import com.fullstack.movies.backend.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Optional<Movie> findByImDbId(String imDbId);

    Set<Movie> findAllByGenre(MovieGenre genre);

    Set<Movie> findAllByNameContainingIgnoreCase(String name);
}
