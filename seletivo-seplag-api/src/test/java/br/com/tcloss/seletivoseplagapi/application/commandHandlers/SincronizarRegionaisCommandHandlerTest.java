package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.tcloss.seletivoseplagapi.application.dtos.RegionalDto;
import br.com.tcloss.seletivoseplagapi.application.ports.IntegradorRegionaisService;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.MapaRegionais;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.RegionalRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.*;

@ExtendWith(MockitoExtension.class)
public class SincronizarRegionaisCommandHandlerTest {

    @Mock
    IntegradorRegionaisService integradorRegionaisService;

    @Mock
    RegionalRepository regionalRepository;

    @InjectMocks
    SincronizarRegionaisCommandHandler sincronizarRegionaisCommandHandler;

    @Test
    @DisplayName("Deve inserir todas as Regionais quando o banco estiver vazio")
    public void shouldInsertAllRegionalDataWhenDataBaseIsEmpty() {
        List<RegionalDto> regionaisFakeApi = List.of(
                new RegionalDto(1, "teste 1"),
                new RegionalDto(2, "teste 2"),
                new RegionalDto(3, "teste 1"));
        Mockito.when(integradorRegionaisService.buscarRegionais()).thenReturn(regionaisFakeApi);
        Mockito.when(regionalRepository.regionaisAtivas()).thenReturn(new MapaRegionais(List.of()));

        var response = sincronizarRegionaisCommandHandler.execute();

        assertThat(response.alteradas()).isEmpty();
        assertThat(response.removidas()).isEmpty();
        assertThat(response.novas()).hasSize(regionaisFakeApi.size());
    }

    @Test
    @DisplayName("Deve subistituir todas as Regionais")
    public void shouldReplaceAllRegionals() {
        List<RegionalDto> regionaisFakeApi = List.of(
                new RegionalDto(1, "teste 1"),
                new RegionalDto(2, "teste 2"),
                new RegionalDto(3, "teste 1"));
        List<Regional> regionaisFakeDb = List.of(
                new Regional(1, "teste 1 x"),
                new Regional(2, "teste 2 x"),
                new Regional(3, "teste 1 x"));
        Mockito.when(integradorRegionaisService.buscarRegionais()).thenReturn(regionaisFakeApi);
        Mockito.when(regionalRepository.regionaisAtivas()).thenReturn(new MapaRegionais(regionaisFakeDb));

        var response = sincronizarRegionaisCommandHandler.execute();

        assertThat(response.novas()).isEmpty();
        assertThat(response.removidas()).isEmpty();
        assertThat(response.alteradas()).hasSize(regionaisFakeApi.size());
    }

    @Test
    @DisplayName("Deve inserir uma, remover uma e alterar uma")
    public void shouldInsertUpdateAndRemove() {
        List<RegionalDto> regionaisFakeApi = List.of(
                new RegionalDto(1, "teste 1"),
                new RegionalDto(2, "teste 2"));
        List<Regional> regionaisFakeDb = List.of(
                new Regional(2, "teste 2 x"),
                new Regional(3, "teste 3"));
        Mockito.when(integradorRegionaisService.buscarRegionais()).thenReturn(regionaisFakeApi);
        Mockito.when(regionalRepository.regionaisAtivas()).thenReturn(new MapaRegionais(regionaisFakeDb));

        var response = sincronizarRegionaisCommandHandler.execute();

        assertThat(response.novas()).hasSize(1);
        assertThat(response.removidas()).hasSize(1);
        assertThat(response.alteradas()).hasSize(1);
        assertThat(response.alteradas().get(0).getNome()).isEqualTo("teste 2");
        assertThat(response.removidas().get(0).getNome()).isEqualTo("teste 3");
        assertThat(response.novas().get(0).getNome()).isEqualTo("teste 1");
        
    }
}
