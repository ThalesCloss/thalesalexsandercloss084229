package br.com.tcloss.seletivoseplagapi.infra.persistence.repository.panache;

import java.util.Optional;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfile;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ArtistProfileRepositoryPanache implements ArtistProfileRepository, PanacheRepositoryBase<ArtistProfile, UUID>{

    @Override
    public void save(ArtistProfile artistProfile) {
        persist(artistProfile);
    }

    @Override
    public Optional<ArtistProfile> getById(UUID uuid) {
        return findByIdOptional(uuid);
    }

}
