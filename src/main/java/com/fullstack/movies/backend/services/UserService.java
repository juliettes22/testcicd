package com.fullstack.movies.backend.services;

import com.fullstack.movies.backend.exceptions.ConflictException;
import com.fullstack.movies.backend.exceptions.errors.UserErrorCode;
import com.fullstack.movies.backend.models.converters.UserConverter;
import com.fullstack.movies.backend.models.dtos.UserDto;
import com.fullstack.movies.backend.models.dtos.UserRegistrationDto;
import com.fullstack.movies.backend.models.entities.Library;
import com.fullstack.movies.backend.models.entities.User;
import com.fullstack.movies.backend.repositories.LibraryRepository;
import com.fullstack.movies.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder encoder;

    /**
     * Register a new user
     * @param dto
     * @return
     */
    @Transactional
    public UserDto registerUser(UserRegistrationDto dto) {
        log.info("Registering user : {}", dto.getUsername());

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new ConflictException(UserErrorCode.EMAIL_ALREADY_EXISTS, dto.getEmail());
                });

        String encodedPassword = encoder.encode(dto.getPassword());

        Library library = libraryRepository.save(Library.builder().build());

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .library(library)
                .build();

        return userConverter.convert(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        log.info("Getting user by id : {}", userId);

        return userRepository.findById(userId)
                .map(userConverter::convert)
                .orElseThrow(() -> new ConflictException(UserErrorCode.NOT_FOUND, userId.toString()));
    }

}
