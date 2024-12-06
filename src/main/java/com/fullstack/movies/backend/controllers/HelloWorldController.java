package com.fullstack.movies.backend.controllers;

import com.fullstack.movies.backend.models.dtos.MyDto;
import com.fullstack.movies.backend.services.MyEntityService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/hello-world", produces = "application/json")
@RequiredArgsConstructor
public class HelloWorldController {

    private final MyEntityService myEntityService;

    private record StringResponse(String response) {}

    /**
     * covers the endpoint GET /hello-world
     * @return Hello World
     */
    @ApiResponse(responseCode = "200", description = "Hello World")
    @GetMapping
    public ResponseEntity<StringResponse> getHelloWorld() {
        log.info("Endpoint /hello-world reached");
        return ResponseEntity.ok(new StringResponse("Hello World!"));
    }

    /**
     * covers the endpoint GET /hello-world/name?name=
     * @return Hello + name
     */
    @GetMapping("/name")
    public ResponseEntity<StringResponse> getHelloName(@RequestParam String name) {
        log.info("Endpoint /hello-world/name?name reached with name {}", name);
        return ResponseEntity.ok(new StringResponse("Hello " + name + "!"));
    }

    /**
     * covers the endpoint GET /hello-world/name/optional?name=
     * @return Hello + name || Hello World
     */
    @GetMapping("/name/optional")
    public ResponseEntity<StringResponse> getHelloOptionalName(@RequestParam @Nullable String name) {
        log.info("Endpoint /hello-world/name/optional?name reached with name {}", name);
        if (name != null) {
            return ResponseEntity.ok(new StringResponse("Hello " + name + "!"));
        } else {
            return ResponseEntity.ok(new StringResponse("Hello World!"));
        }
    }

    /**
     * covers the endpoint GET /hello-world/name/{name}
     *
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<StringResponse> getHelloNamePathVariable(@PathVariable String name) {
        log.info("Endpoint /hello-world/name/{name} with name {}", name);
        return ResponseEntity.ok(new StringResponse("Hello " + name + "!"));
    }

    /**
     * covers the endpoint POST /hello-world/entity
     * @return MyDto
     */
    @PostMapping("/entity")
    public ResponseEntity<MyDto> registerMyEntity(@RequestBody @Valid MyDto dto) {
        log.info("Endpoint /hello-world/entity reached to register a new entity : {}", dto);
        return ResponseEntity.ok(myEntityService.registerMyEntity(dto));
    }


    @GetMapping("/findAllByAge")
    public ResponseEntity<List<MyDto>> findAllByAge(@RequestParam Integer age) {
        log.info("Endpoint /hello-world/findAllByAge?age reached with age {}", age);
        return ResponseEntity.ok(myEntityService.getAllByAge(age));
    }

    @GetMapping("/findAllByNameNative")
    public ResponseEntity<List<MyDto>> findAllByNameNative(@RequestParam String name) {
        log.info("Endpoint /hello-world/findAllByNameNative?name reached with name {}", name);
        return ResponseEntity.ok(myEntityService.findAllByNameNative(name));
    }

}
