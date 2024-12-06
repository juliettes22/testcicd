package com.fullstack.movies.backend.repositories;

import com.fullstack.movies.backend.models.constants.MovieGenre;
import com.fullstack.movies.backend.models.entities.LibraryMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface LibraryMovieRepository extends JpaRepository<LibraryMovie, LibraryMovie.LibraryMovieKeyId> {

    Set<LibraryMovie> findAllByLibraryId(UUID libraryId);

    Set<LibraryMovie> findAllByMovieGenre(MovieGenre genre);
}
