package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import java.util.Optional;
import java.util.UUID;

public interface CompositionRepository {
    void save(Composition composition);
    Optional<Composition> getById(UUID id);
}
