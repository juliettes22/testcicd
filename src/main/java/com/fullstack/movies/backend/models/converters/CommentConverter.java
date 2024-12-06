package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.CommentDto;
import com.fullstack.movies.backend.models.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentConverter implements Converter<Comment, CommentDto> {

    private final MovieConverter movieConverter;
    private final UserConverter userConverter;

    @Override
    public CommentDto convert(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId().toString())
                .title(comment.getTitle())
                .content(comment.getContent())
                .movieId(comment.getMovie().getId())
                .username(comment.getUser().getUsername())
                .publishedAt(comment.getCreatedAt())
                .build();
    }
}
