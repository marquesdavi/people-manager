package com.api.manager.people.service;

import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;

public interface PersonService {
    void create(PersonRequest request);

    PersonResponse getById(Long id);
}
