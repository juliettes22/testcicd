package com.fullstack.movies.backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

        private String id;

        private String title;

        private String content;

        private UUID movieId;

        private String username;

        private Instant publishedAt;
}
