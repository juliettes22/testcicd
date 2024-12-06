package com.fullstack.movies.backend.models.converters;

import com.fullstack.movies.backend.models.dtos.MyDto;
import com.fullstack.movies.backend.models.entities.MyEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component // This annotation is used to mark this class as a Spring component, one Bean instance will be created
public class MyEntityConverter implements Converter<MyEntity, MyDto> {

    @Override
    public MyDto convert(MyEntity source) {
        return MyDto.builder()
                .name(source.getName())
                .age(source.getAge())
                .build();
    }
}
