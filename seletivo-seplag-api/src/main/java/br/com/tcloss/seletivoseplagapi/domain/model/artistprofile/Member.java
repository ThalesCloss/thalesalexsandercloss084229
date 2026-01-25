package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;

public record Member(
    UUID personId,
    boolean isMainArtist,
    boolean isFounder
) implements ValueObject{

}
