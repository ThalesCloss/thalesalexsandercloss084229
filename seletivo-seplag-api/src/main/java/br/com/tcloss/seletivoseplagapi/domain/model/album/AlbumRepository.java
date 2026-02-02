package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository {
    public void save(Album album);

    public Optional<Album> getById(UUID albumId);
}
