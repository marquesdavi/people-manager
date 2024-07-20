package com.api.manager.people.service.impl;

import com.api.manager.people.domain.person.Person;
import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import com.api.manager.people.exception.AlreadyExistsException;
import com.api.manager.people.exception.NotFoundException;
import com.api.manager.people.mapper.PersonMapper;
import com.api.manager.people.repository.IPersonRepository;
import com.api.manager.people.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImplementation implements PersonService {
    private final IPersonRepository repository;

    @Override
    public void create(PersonRequest request) {
        log.info("Creating person: {}", request);
        checkIfPersonExistsByCpf(request.cpf());

        Person person = PersonMapper.toEntity(request);
        if (!request.email().isEmpty()) {
            person.setEmail(request.email());
        }

        repository.save(person);

        log.info("Person created successfully");
    }

    @Override
    public PersonResponse getById(Long id) {
        Person person = findPersonById(id);
        return PersonMapper.toDTO(person);
    }

    public Person findPersonById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = "The Person with ID " + id + " doesn't exist!";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    private void checkIfPersonExistsByCpf(String cpf) {
        if (repository.findByCpf(cpf).isPresent()) {
            String message = "A Person with CPF " + cpf + " already exists!";
            log.error(message);
            throw new AlreadyExistsException(message);
        }
    }
}
