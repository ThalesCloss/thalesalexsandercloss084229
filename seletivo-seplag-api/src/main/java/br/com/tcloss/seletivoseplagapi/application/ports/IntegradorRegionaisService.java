package br.com.tcloss.seletivoseplagapi.application.ports;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.tcloss.seletivoseplagapi.application.dtos.RegionalDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient(configKey = "api-regionais")
public interface IntegradorRegionaisService {

    @GET
    @Path("/v1/regionais")
    List<RegionalDto> buscarRegionais();

}
