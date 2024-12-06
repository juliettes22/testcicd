package com.fullstack.movies.backend.configurations.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("custom")
public class MyCustomProperties {

    @NotNull
    private Integer value;
}
