package br.com.tcloss.seletivoseplagapi.domain.model.regional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapaRegionais {
    private final Map<Integer, Regional> mapa;
    private final List<Regional> inseridas = new ArrayList<>();
    private final List<Regional> alteradas = new ArrayList<>();
    private final List<Regional> removidas = new ArrayList<>();

    public MapaRegionais(List<Regional> regionais) {
        mapa = regionais.stream().collect(Collectors.toMap(Regional::getId, val -> val));
    }

    public Regional obterRegionalExistente(int id) {
        return mapa.get(id);
    }

    public void marcarRegionalInalterada(Regional regional) {
        mapa.remove(regional.getId());
    }

    public void inserirRegional(Regional regional) {
        inseridas.add(regional);
    }

    public void alterarRegional(Regional regionalExistente, Regional regionalNova) {
        mapa.remove(regionalExistente.getId());
        alteradas.add(regionalNova);
    }

    public void removerRegional(Regional regional) {
        removidas.add(regional);
    }

    public Collection<Regional> obterRegionaisExcedentes() {
        return mapa.values();
    }

    public List<Regional> getInseridas() {
        return Collections.unmodifiableList(inseridas);
    }

    public List<Regional> getAlteradas() {
        return Collections.unmodifiableList(alteradas);
    }

    public List<Regional> getRemovidas() {
        return Collections.unmodifiableList(removidas);
    }

}
