package com.fullstack.movies.backend.repositories;

import com.fullstack.movies.backend.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findAllByMovieIdOrderByCreatedAtAsc(UUID movieId);
}
