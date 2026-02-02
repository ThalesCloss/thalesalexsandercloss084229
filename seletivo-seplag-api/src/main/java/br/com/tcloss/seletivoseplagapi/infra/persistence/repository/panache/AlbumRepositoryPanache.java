package br.com.tcloss.seletivoseplagapi.infra.persistence.repository.panache;

import java.util.Optional;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.album.Album;
import br.com.tcloss.seletivoseplagapi.domain.model.album.AlbumRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlbumRepositoryPanache implements AlbumRepository, PanacheRepositoryBase<Album, UUID> {

    @Override
    public void save(Album album) {
        persist(album);
    }

    @Override
    public Optional<Album> getById(UUID albumId) {
        return findByIdOptional(albumId);
    }

}
