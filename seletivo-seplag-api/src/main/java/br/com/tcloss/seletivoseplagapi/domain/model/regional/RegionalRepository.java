package br.com.tcloss.seletivoseplagapi.domain.model.regional;


public interface RegionalRepository {
    public void gravar(Regional regional);

    public MapaRegionais regionaisAtivas();

    

}
