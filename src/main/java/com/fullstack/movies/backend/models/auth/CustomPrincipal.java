package com.fullstack.movies.backend.models.auth;

import com.fullstack.movies.backend.models.entities.User;
import lombok.*;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal {

    public CustomPrincipal (User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.libraryId = user.getLibrary().getId();
    }

    private UUID id;
    private String username;
    private String email;
    private UUID libraryId;
}
