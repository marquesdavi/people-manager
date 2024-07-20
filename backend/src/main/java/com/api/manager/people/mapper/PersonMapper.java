package com.api.manager.people.mapper;

import com.api.manager.people.domain.person.Person;
import com.api.manager.people.dto.person.PersonRequest;
import com.api.manager.people.dto.person.PersonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

public class PersonMapper {
    public static PersonResponse toDTO(Person person) {
        if (person == null) {
            return null;
        }
        return new PersonResponse(
                person.getId(),
                person.getName(),
                person.getEmail(),
                person.getCpf(),
                person.getBirthDate()
        );
    }

    public static Person toEntity(PersonRequest personRequest) {
        if (personRequest == null) {
            return null;
        }
        Person person = new Person();
        BeanUtils.copyProperties(personRequest, person);
        return person;
    }
}
