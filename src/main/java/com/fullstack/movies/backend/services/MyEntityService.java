package com.fullstack.movies.backend.services;

import com.fullstack.movies.backend.configurations.properties.MyCustomProperties;
import com.fullstack.movies.backend.exceptions.NotFoundException;
import com.fullstack.movies.backend.exceptions.errors.MyEntityErrorCode;
import com.fullstack.movies.backend.models.converters.MyEntityConverter;
import com.fullstack.movies.backend.models.dtos.MyDto;
import com.fullstack.movies.backend.models.entities.MyEntity;
import com.fullstack.movies.backend.repositories.MyEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service // used to mark this class as a Spring service
@RequiredArgsConstructor // Lombok will generate a constructor with all the required fields
public class MyEntityService {

    private final MyEntityRepository myEntityRepository;
    private final MyEntityConverter myEntityConverter;
    private final MyCustomProperties customProperties;

    @Value("${custom.value}")
    private Integer customValue;

    @Transactional
    public MyDto registerMyEntity(MyDto dto) {
        log.info("Registering a new entity : {}", dto);

        MyEntity myEntity = MyEntity.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .build();

        return myEntityConverter.convert(myEntityRepository.save(myEntity));
    }

    @Transactional(readOnly = true)
    public MyDto getById(UUID id) {
        log.info("Finding entity by id : {}", id);

        return myEntityRepository.findById(id)
                .map(myEntityConverter::convert)
                .orElseThrow(() -> new NotFoundException(MyEntityErrorCode.NOT_FOUND, id));
    }

    @Transactional(readOnly = true)
    public List<MyDto> getAllByAge(int age) {
        log.info("Finding all entities by age : {}", age);

        List<MyEntity> myEntities = myEntityRepository.findAllByAge(age);

        return myEntities.stream().map(myEntityConverter::convert).toList();
    }

    @Transactional(readOnly = true)
    public List<MyDto> findAllByNameNative(String name) {
        log.info("Finding all entities by name : {}", name);

        List<MyEntity> myEntities = myEntityRepository.findAllByNameNative(name);

        return myEntities.stream().map(myEntityConverter::convert).toList();
    }

    public void getCustomProperties() {
        log.info("Custom value : {}", customValue);
        log.info("Custom properties : {}", customProperties.getValue());
    }
}
