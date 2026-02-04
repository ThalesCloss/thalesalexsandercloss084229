package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.util.Optional;
import java.util.UUID;

public interface ArtistProfileRepository {
    void save(ArtistProfile artistProfile);
    Optional<ArtistProfile> getById(UUID uuid);
}
