package com.api.manager.people.repository;

import com.api.manager.people.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByCpf(String cpf);
}
