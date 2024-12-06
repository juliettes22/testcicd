package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.UserDto;
import com.fullstack.movies.backend.models.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class UserConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User user) {

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .libraryId(user.getLibrary().getId())
                .build();
    }
}
