package br.com.tcloss.seletivoseplagapi.application.commandHandlers;


import br.com.tcloss.seletivoseplagapi.application.commands.CreatePersonCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.person.Person;
import br.com.tcloss.seletivoseplagapi.domain.model.person.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreatePersonCommandHandler {
    private final PersonRepository personRepository;

    @Transactional
    public Person execute(CreatePersonCommand request) {
        final var person = Person.createNew(
                request.name(),
                request.birthDate()
        );
        personRepository.save(person);
        return person;
    }
}
