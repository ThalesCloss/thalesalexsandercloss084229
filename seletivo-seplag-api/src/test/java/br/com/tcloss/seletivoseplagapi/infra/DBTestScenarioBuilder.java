package br.com.tcloss.seletivoseplagapi.infra;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfile;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Lineup;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Member;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.Composition;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.CompositionRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.person.Person;
import br.com.tcloss.seletivoseplagapi.domain.model.person.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@ApplicationScoped
public class DBTestScenarioBuilder {
    @Inject
    CompositionRepository compositionRepository;
    @Inject
    ArtistProfileRepository artistProfileRepository;
    @Inject
    PersonRepository personRepository;
    private final Faker faker = new Faker(Locale.of("pt", "BR"));

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Person createPerson() {
        final var person = Person.createNew(faker.name().fullName().toString(),
                faker.date().birthday(20, 60).toLocalDateTime().toLocalDate());
        personRepository.save(person);

        return person;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Composition createComposition(Person... persons) {
        final var composition = Composition.createNew("Composicao teste", "T-123.456.789-0",
                Stream.of(persons).map(p -> p.getId()).toList(), null,
                faker.date().birthday(20, 60).toLocalDateTime().toLocalDate());
        compositionRepository.save(composition);
        return composition;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ArtistProfile createSoloArtistProfile(Person person) {
        final var startDate = faker.date().birthday(3, 10).toLocalDateTime().toLocalDate();
        final var artist = ArtistProfile.createNew(
                person.getName().name(),
                faker.lorem().paragraph(),
                ArtistType.SOLO,
                List.of(Lineup.createNew(
                        "Formação original",
                        List.of(new Member(person.getId(), true, true)),
                        startDate,
                        null)));
        artistProfileRepository.save(artist);

        return artist;
    }
}
