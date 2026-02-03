package br.com.tcloss.seletivoseplagapi.application.mappers;

import java.time.Duration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


import br.com.tcloss.seletivoseplagapi.application.dtos.output.ImageResponse;
import br.com.tcloss.seletivoseplagapi.application.ports.FileManager;
import br.com.tcloss.seletivoseplagapi.application.ports.AlbumQueryService.ImageDataProjection;
import jakarta.inject.Inject;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ImageMapper {

    @Inject
    protected FileManager fileManager;

    @ConfigProperty(name = "application.storage.presignedurl.expiration")
    protected Duration expiration;

    public ImageResponse toResponse(ImageDataProjection projection) {
        if (projection == null || projection.imageUrl() == null || projection.imageUrl().isEmpty()) {
            return null;
        }

        var signedUrl = fileManager.generatePresignedUrl(projection.imageUrl(), expiration);

        if (signedUrl == null) {
            return null;
        }

        return new ImageResponse(signedUrl, projection.type());
    }
}
