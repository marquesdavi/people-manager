package com.api.manager.people.service.impl;

import com.api.manager.people.domain.person.Person;
import com.api.manager.people.exception.AlreadyExistsException;
import com.api.manager.people.exception.NotFoundException;
import com.api.manager.people.mapper.PersonMapper;
import com.api.manager.people.model.dto.person.PersonRequest;
import com.api.manager.people.model.dto.person.PersonResponse;
import com.api.manager.people.model.vo.DefaultResponse;
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

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public DefaultResponse updatePerson(Long id, PersonRequest personRequest) {
        log.info("Updating person with ID {}", id);
        Person existingPerson = findPersonById(id);

        if (personRequest.name() != null) {
            existingPerson.setName(personRequest.name());
        }

        if (personRequest.email() != null) {
            existingPerson.setEmail(personRequest.email());
        }

        if (personRequest.cpf() != null) {
            validateCpf(personRequest.cpf());
            existingPerson.setCpf(personRequest.cpf());
        }

        if (personRequest.birthDate() != null) {
            existingPerson.setBirthDate(personRequest.birthDate());
        }

        repository.save(existingPerson);

        log.info("Person with ID {} successfully updated", id);
        return new DefaultResponse(true, "Person successfully updated!");
    }

    @Override
    public DefaultResponse deletePerson(Long id) {
        log.info("Deleting person with ID {}", id);

        Person existingPerson = findPersonById(id);
        repository.delete(existingPerson);

        log.info("Person with ID {} deleted successfully", id);
        return new DefaultResponse(true, "Person successfully deleted!");
    }

    @Override
    public PersonResponse getById(Long id) {
        Person person = findPersonById(id);
        return PersonMapper.toDTO(person);
    }

    @Override
    public Map<String, Object> getAll(
            @PositiveOrZero Integer startRow,
            @Positive Integer endRow,
            String orderBy,
            String direction
    ) {
        validateOrderBy(Person.class, orderBy);
        Sort.Direction sortDirection = validateSortDirection(direction);

        if (startRow > endRow){
            log.error("startRow must be less than endRow");
            throw new IllegalArgumentException("startRow must be less than endRow");
        }

        int page = startRow / (endRow - startRow);
        int size = endRow - startRow;
        PageRequest pageable = PageRequest.of(page, size, sortDirection, orderBy);

        Page<Person> persons = repository.findAll(pageable);
        long totalRows = repository.count();

        List<PersonResponse> personResponses = persons.stream()
                .map(PersonMapper::toDTO)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("lastRow", totalRows);
        response.put("rows", personResponses);

        return response;
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
