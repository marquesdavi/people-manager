package com.api.manager.people.controller;

import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import com.api.manager.people.service.PersonService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found")})
    @GetMapping("/{id}")
    public PersonResponse getById(@PathVariable(name = "id") Long id) {
        return personService.getById(id);
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createPerson(
            @RequestBody PersonRequest person
    ){
        personService.create(person);
    }
}
