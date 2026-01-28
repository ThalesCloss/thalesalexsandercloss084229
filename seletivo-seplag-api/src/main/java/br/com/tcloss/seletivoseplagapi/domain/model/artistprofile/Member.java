package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Member(
    @Column(name = "person_id", nullable = false)
    UUID personId,

    @Column(name = "is_main_artist", nullable = false)
    boolean isMainArtist,

    @Column(name = "is_founder", nullable = false)
    boolean isFounder
) implements ValueObject{

}
