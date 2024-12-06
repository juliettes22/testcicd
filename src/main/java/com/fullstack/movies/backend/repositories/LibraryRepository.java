package com.fullstack.movies.backend.repositories;

import com.fullstack.movies.backend.models.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LibraryRepository extends JpaRepository<Library, UUID> {

    Optional<Library> findByUserId(UUID userId);

}
