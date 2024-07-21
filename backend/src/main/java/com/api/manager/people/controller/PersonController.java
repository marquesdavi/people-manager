package com.api.manager.people.controller;

import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import com.api.manager.people.service.PersonService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "200", description = "Person found")})
    @GetMapping("/{id}")
    public PersonResponse getById(@PathVariable(name = "id") Long id) {
        return personService.getById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied"),
            @ApiResponse(responseCode = "404", description = "People not found"),
            @ApiResponse(responseCode = "200", description = "People found")})
    @GetMapping("/")
    public ResponseEntity<List<PersonResponse>> getAllPeople(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {

        return ResponseEntity.ok().body(personService.getAll(page, size, orderBy, direction));
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createPerson(
            @Valid @RequestBody PersonRequest person
    ){
        personService.createPerson(person);
    }
}
