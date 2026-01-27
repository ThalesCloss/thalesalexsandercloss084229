package br.com.tcloss.seletivoseplagapi.infra.persistence.repository.panache;

import java.util.Optional;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.composition.Composition;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.CompositionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompositionRepositoryPanache implements CompositionRepository, PanacheRepositoryBase<Composition, UUID> {

    @Override
    public void save(Composition composition) {
        persist(composition);
    }

    @Override
    public Optional<Composition> getById(UUID id) {
        return findByIdOptional(id);
    }

}
