package br.com.tcloss.seletivoseplagapi.application.commandHandlers;


import org.eclipse.microprofile.rest.client.inject.RestClient;


import br.com.tcloss.seletivoseplagapi.application.dtos.RegionalDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.SincronizacaoResponse;
import br.com.tcloss.seletivoseplagapi.application.ports.IntegradorRegionaisService;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.MapaRegionais;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.Regional;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.RegionalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SincronizarRegionaisCommandHandler {
    private final RegionalRepository regionalRepository;
    private final IntegradorRegionaisService integradorRegionaisService;

    public SincronizarRegionaisCommandHandler(RegionalRepository regionalRepository,
            @RestClient IntegradorRegionaisService integradorRegionaisService) {
        this.regionalRepository = regionalRepository;
        this.integradorRegionaisService = integradorRegionaisService;
    }

    @Transactional
    public SincronizacaoResponse execute() {
        final var regionaisIntegrador = integradorRegionaisService.buscarRegionais();
        final var mapaRegionais = regionalRepository.regionaisAtivas();

        for (var regional : regionaisIntegrador) {
            processarSincronizacaoRegional(mapaRegionais, regional);
        }

        for (var regionalDesativar : mapaRegionais.obterRegionaisExcedentes()) {
            mapaRegionais.removerRegional(regionalDesativar);
            regionalDesativar.desativar();
        }

        return new SincronizacaoResponse(mapaRegionais.getInseridas(), mapaRegionais.getRemovidas(),
                mapaRegionais.getAlteradas());
    }

   

    private void processarSincronizacaoRegional(final MapaRegionais mapaRegionaisAtivas, RegionalDto regional) {
        final var regionalExistente = mapaRegionaisAtivas.obterRegionalExistente(regional.id());

        if (regionalExistente == null) {
            mapaRegionaisAtivas.inserirRegional(criarRegional(regional));
            return;
        }
        if (regionalExistente.nomeFoiAlterado(regional.nome())) {
            mapaRegionaisAtivas.alterarRegional(regionalExistente, criarRegional(regional));
            regionalExistente.desativar();
            return;
        }
        mapaRegionaisAtivas.marcarRegionalInalterada(regionalExistente);

    }

    private Regional criarRegional(final RegionalDto regional) {
        final var regionalNova = new Regional(regional.id(), regional.nome());
        regionalRepository.gravar(regionalNova);
        return regionalNova;
    }
}
