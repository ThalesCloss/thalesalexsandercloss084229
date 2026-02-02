package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumImageCommand;
import br.com.tcloss.seletivoseplagapi.application.ports.FileManager;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Album;
import br.com.tcloss.seletivoseplagapi.domain.model.album.AlbumRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Image;
import br.com.tcloss.seletivoseplagapi.domain.model.album.ImageType;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreateAlbumImageCommandHandler {
    private final AlbumRepository albumRepository;
    private final FileManager fileManager;

    public void execute(CreateAlbumImageCommand command) {
        final var filename = fileManager.upload(command.image().file().uploadedFile(),
                command.image().file().fileName(), command.image().file().contentType());
        try {
            addAlbumImage(command.albumId(), filename, command.image().type());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void addAlbumImage(UUID albumId, String image, ImageType imageType) {
        final var albumResponse = albumRepository.getById(albumId);
        if (albumResponse.isEmpty()) {
            throw new DomainException("O álbum não existe");
        }
        final Album album = (Album) albumResponse.get();
        album.addImage(new Image(image, " jpg ", imageType));
        albumRepository.save(album);
    }
}
