package com.fullstack.movies.backend.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPublishDto {

    @NotEmpty
    @Size(min = 1, max = 255)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 1000)
    private String content;

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID userId;
}
