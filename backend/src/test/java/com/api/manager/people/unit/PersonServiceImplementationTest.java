package com.api.manager.people.unit;

import com.api.manager.people.domain.person.Person;
import com.api.manager.people.exception.AlreadyExistsException;
import com.api.manager.people.exception.NotFoundException;
import com.api.manager.people.mapper.PersonMapper;
import com.api.manager.people.model.dto.person.PersonRequest;
import com.api.manager.people.model.dto.person.PersonResponse;
import com.api.manager.people.model.vo.DefaultResponse;
import com.api.manager.people.repository.IPersonRepository;
import com.api.manager.people.service.impl.PersonServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplementationTest {

    @Mock
    private IPersonRepository repository;

    @InjectMocks
    private PersonServiceImplementation service;

    private Person person;
    private PersonRequest personRequest;

    @BeforeEach
    void setUp() {
        person = new Person(1L, "John Doe", "john.doe@example.com", "12345678909", LocalDate.of(1980, 1, 1));
        personRequest = new PersonRequest("Jane Doe", "jane.doe@example.com", "98765432100", LocalDate.of(1990, 2, 1));
    }

    @Test
    @DisplayName("Create person should save person")
    void createPerson_shouldSavePerson() {
        when(repository.save(any(Person.class))).thenReturn(person);

        service.createPerson(personRequest);

        verify(repository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Create person should throw AlreadyExistsException")
    void createPerson_shouldThrowAlreadyExistsException() {
        when(repository.findByCpf(personRequest.cpf())).thenReturn(Optional.of(person));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            service.createPerson(personRequest);
        });

        assertTrue(exception.getMessage().contains("A Person with CPF 98765432100 already exists!"));
    }

    @Test
    @DisplayName("Update person should update existing person")
    void updatePerson_shouldUpdateExistingPerson() {
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        DefaultResponse response = service.updatePerson(1L, personRequest);

        verify(repository, times(1)).save(any(Person.class));
        assertEquals("Person successfully updated!", response.message());
    }

    @Test
    @DisplayName("Update person should throw NotFoundException")
    void updatePerson_shouldThrowNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.updatePerson(1L, personRequest);
        });

        assertEquals("The Person with ID 1 doesn't exist!", exception.getMessage());
    }

    @Test
    @DisplayName("Delete person should delete existing person")
    void deletePerson_shouldDeleteExistingPerson() {
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        DefaultResponse response = service.deletePerson(1L);

        verify(repository, times(1)).delete(person);
        assertEquals("Person successfully deleted!", response.message());
    }

    @Test
    @DisplayName("Delete person should throw NotFoundException")
    void deletePerson_shouldThrowNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.deletePerson(1L);
        });

        assertEquals("The Person with ID 1 doesn't exist!", exception.getMessage());
    }

    @Test
    @DisplayName("Get person by ID should return person response")
    void getById_shouldReturnPersonResponse() {
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        PersonResponse response = service.getById(1L);

        assertNotNull(response);
        assertEquals(person.getName(), response.name());
    }

    @Test
    @DisplayName("Get person by ID should throw NotFoundException")
    void getById_shouldThrowNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getById(1L);
        });

        assertEquals("The Person with ID 1 doesn't exist!", exception.getMessage());
    }

    @Test
    @DisplayName("Get all persons should return page of person responses")
    void getAll_shouldReturnPageOfPersonResponses() {
        Page<Person> page = new PageImpl<>(List.of(person));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        List<PersonResponse> responses = service.getAll(0, 10, "name", "ASC");

        assertFalse(responses.isEmpty());
        assertEquals(person.getName(), responses.get(0).name());
    }

}
