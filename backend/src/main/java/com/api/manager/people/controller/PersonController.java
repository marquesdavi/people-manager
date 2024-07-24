package com.api.manager.people.controller;

import com.api.manager.people.model.dto.person.PersonRequest;
import com.api.manager.people.model.dto.person.PersonResponse;
import com.api.manager.people.model.vo.DefaultResponse;
import com.api.manager.people.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
@PreAuthorize("hasAuthority('USER')")
@Tag(name = "Person", description = "Person management")
public class PersonController {
    private final PersonService personService;

    @Operation(summary = "Get person by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{id}")
    @Cacheable(value = "person-find-by-id")
    public ResponseEntity<PersonResponse> getById(@PathVariable(name = "id") Long id) {
        PersonResponse personResponse = personService.getById(id);
        return ResponseEntity.ok(personResponse);
    }

    @Operation(summary = "Get all people with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "People found"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "404", description = "People not found")
    })
    @GetMapping("/")
    @Cacheable(value = "people-find-all")
    public ResponseEntity<Map<String, Object>> getAllPeople(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "10") Integer endRow,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Map<String, Object> response = personService.getAll(startRow, endRow, orderBy, direction);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    public ResponseEntity<DefaultResponse> createPerson(
            @Valid @RequestBody PersonRequest person
    ) {
        personService.createPerson(person);
        return new ResponseEntity<>(new DefaultResponse(true, "Person successfully created!"), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DefaultResponse> updatePerson(
            @PathVariable(name = "id") Long id, @RequestBody PersonRequest person
    ) {
        DefaultResponse response = personService.updatePerson(id, person);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> deletePerson(
            @PathVariable(name = "id") Long id
    ) {
        DefaultResponse response = personService.deletePerson(id);
        return ResponseEntity.ok(response);
    }
}
