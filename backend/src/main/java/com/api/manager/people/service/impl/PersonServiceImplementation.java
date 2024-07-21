package com.api.manager.people.service.impl;

import com.api.manager.people.domain.person.Person;
import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import com.api.manager.people.exception.AlreadyExistsException;
import com.api.manager.people.exception.NotFoundException;
import com.api.manager.people.mapper.PersonMapper;
import com.api.manager.people.repository.IPersonRepository;
import com.api.manager.people.service.PersonService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.api.manager.people.util.validation.GenericValidation.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImplementation implements PersonService {
    private final IPersonRepository repository;

    @Override
    public void createPerson(PersonRequest request) {
        log.info("Creating person: {}", request);

        validateCpf(request.cpf());
        checkIfPersonExistsByCpf(request.cpf());

        Person person = PersonMapper.toEntity(request);
        if (request.email() != null) {
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

    @Override
    public List<PersonResponse> getAll(
            @PositiveOrZero Integer page,
            @Positive Integer size,
            String orderBy,
            String direction
    ) {
        validateOrderBy(Person.class, orderBy);
        Sort.Direction sortDirection = validateSortDirection(direction);

        PageRequest pageable = PageRequest.of(page, size, sortDirection, orderBy);
        Page<Person> persons = repository.findAll(pageable);

        return persons.stream().map(PersonMapper::toDTO).toList();
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
