package com.api.manager.people.service;

import com.api.manager.people.model.dto.person.PersonRequest;
import com.api.manager.people.model.dto.person.PersonResponse;
import com.api.manager.people.model.vo.DefaultResponse;

import java.util.List;
import java.util.Map;

public interface PersonService {
    void createPerson(PersonRequest request);
    PersonResponse getById(Long id);
    List<PersonResponse> getAll(Integer page, Integer size, String orderBy, String direction);
    DefaultResponse updatePerson(Long id, PersonRequest request);
    DefaultResponse deletePerson(Long id);
}
