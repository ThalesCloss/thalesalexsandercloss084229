package br.com.tcloss.seletivoseplagapi.domain.model.person;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    void save(Person person);
    Optional<Person> getById(UUID personId);
}
