package com.api.manager.people.service;

import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonService {
    void createPerson(PersonRequest request);

    PersonResponse getById(Long id);

    List<PersonResponse> getAll(Integer page, Integer size, String orderBy, String direction);
}
