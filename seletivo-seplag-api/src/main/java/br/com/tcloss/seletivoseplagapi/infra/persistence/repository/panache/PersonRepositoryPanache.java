package br.com.tcloss.seletivoseplagapi.infra.persistence.repository.panache;

import java.util.Optional;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.person.Person;
import br.com.tcloss.seletivoseplagapi.domain.model.person.PersonRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepositoryPanache implements PersonRepository, PanacheRepositoryBase<Person, UUID> {

    @Override
    public void save(Person person) {
        persist(person);
    }

    @Override
    public Optional<Person> getById(UUID personId) {
        return findByIdOptional(personId);
    }

}
