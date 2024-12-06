package com.fullstack.movies.backend.services;

import com.fullstack.movies.backend.exceptions.ForbiddenException;
import com.fullstack.movies.backend.exceptions.errors.AuthenticationErrorCode;
import com.fullstack.movies.backend.managers.JwtManager;
import com.fullstack.movies.backend.models.dtos.AuthenticationRequest;
import com.fullstack.movies.backend.models.dtos.AuthenticationResponse;
import com.fullstack.movies.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService  {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtManager jwtManager;

    @Transactional
    public AuthenticationResponse authenticateUser(AuthenticationRequest dto) {
        log.info("Authenticating user : {}", dto.getEmail());

        return userRepository.findByEmail(dto.getEmail())
                .filter(user -> encoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> AuthenticationResponse.builder()
                        .email(user.getEmail())
                        .token(jwtManager.generateToken(user))
                        .expiresIn(jwtManager.getExpirationTime())
                        .build())
                .orElseThrow(() -> new ForbiddenException(AuthenticationErrorCode.FAILED));
    }
}
