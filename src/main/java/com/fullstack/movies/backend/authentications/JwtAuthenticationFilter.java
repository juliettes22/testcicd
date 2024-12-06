package com.fullstack.movies.backend.authentications;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.movies.backend.exceptions.ErrorMessage;
import com.fullstack.movies.backend.exceptions.NotFoundException;
import com.fullstack.movies.backend.exceptions.errors.AuthenticationErrorCode;
import com.fullstack.movies.backend.exceptions.errors.UserErrorCode;
import com.fullstack.movies.backend.managers.JwtManager;
import com.fullstack.movies.backend.models.auth.CustomPrincipal;
import com.fullstack.movies.backend.models.constants.AuthenticationConstants;
import com.fullstack.movies.backend.models.entities.User;
import com.fullstack.movies.backend.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtManager jwtManager;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Optional<String> jwt = extractJwtFromRequest(request);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (jwt.isPresent() && authentication == null) {
                String userEmail = jwtManager.extractUsername(jwt.get());

                User user = this.userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

                if (jwtManager.isTokenValid(jwt.get(), user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            new CustomPrincipal(user),
                            null,
                            List.of()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

            } catch (NotFoundException | SecurityException | SignatureException e) {
                log.error("Could not authenticate user", e);
                // This error happens if the given JWT is invalid
                respondWithUnauthorizedError(request, response, AuthenticationErrorCode.JWT_INVALID);
            } catch (ExpiredJwtException e) {
                log.error("Current JWT has expired", e);
                // This error happens if the given JWT has expired
                respondWithUnauthorizedError(request, response, AuthenticationErrorCode.JWT_EXPIRED);
            }
    }

    private Optional<String> extractJwtFromRequest(HttpServletRequest request) {
        Optional<String> jwtFromHeader = findTokenInHeader(request);

        if (jwtFromHeader.isPresent()) {
            return jwtFromHeader;
        }

        log.trace("No authentication token found");

        return Optional.empty();
    }

    private Optional<String> findTokenInHeader(HttpServletRequest request) {
        String authHeaderValue = request.getHeader(AuthenticationConstants.AUTHORIZATION_HEADER);

        if (StringUtils.isBlank(authHeaderValue)) {
            return Optional.empty();
        }

        if (!authHeaderValue.toLowerCase().startsWith(AuthenticationConstants.BEARER_PREFIX.toLowerCase() + " ")) {
            return Optional.empty();
        }

        log.trace("Found authentication header: {}", AuthenticationConstants.BEARER_PREFIX);

        return Optional.of(authHeaderValue.trim().substring(AuthenticationConstants.BEARER_PREFIX.length() + 1));
    }

    private void respondWithUnauthorizedError(HttpServletRequest request, HttpServletResponse response, AuthenticationErrorCode error) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));

        ErrorMessage errorMessage = ErrorMessage.fromErrorCode(error);
        objectMapper.writeValue(response.getOutputStream(), errorMessage);
    }
}