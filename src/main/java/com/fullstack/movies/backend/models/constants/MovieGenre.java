package com.fullstack.movies.backend.models.constants;

import java.util.Arrays;

public enum MovieGenre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILM_NOIR("Film-Noir"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SHORT_FILM("Short Film"),
    SPORT("Sport"),
    SUPERHERO("Superhero"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western"),
    UNKNOWN("Unknown");

    private final String name;

    MovieGenre(String name) {
        this.name = name;
    }

    public static MovieGenre getByName(String name) {
        return Arrays.stream(MovieGenre.values())
                .filter(genre -> genre.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOWN);
    }
}




