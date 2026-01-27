package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.CreateCompositionCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.Composition;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.CompositionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreateCompositionCommandHandler {
    private final CompositionRepository compositionRepository;

    @Transactional
    public void execute(CreateCompositionCommand request) {
        final var composition = Composition.createNew(
                request.title(),
                request.iswc(),
                request.authorIds(),
                request.lyrics(),
                request.releaseDate()
        );
        compositionRepository.save(composition);
    }
}
