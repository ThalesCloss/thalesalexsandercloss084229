package br.com.tcloss.seletivoseplagapi.infra.persistence.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.Region;

import br.com.tcloss.seletivoseplagapi.domain.model.regional.MapaRegionais;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.Regional;
import br.com.tcloss.seletivoseplagapi.domain.model.regional.RegionalRepository;

@ApplicationScoped
public class RegionalRepositoryPanache implements RegionalRepository, PanacheRepositoryBase<Regional, Long> {

    @Override
    public void gravar(Regional regional) {
        persist(regional);
    }

    @Override
    public MapaRegionais regionaisAtivas() {
        return new MapaRegionais(list("ativo", true));
    }

}
