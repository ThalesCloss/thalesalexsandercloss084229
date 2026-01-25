package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;
@FunctionalInterface
public interface ArtistTypePolicy {
    void validate(ArtistProfile artist, Lineup newLineup);
}
