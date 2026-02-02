package br.com.tcloss.seletivoseplagapi.application.dtos.input.album;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import br.com.tcloss.seletivoseplagapi.application.validations.FileImage;
import br.com.tcloss.seletivoseplagapi.application.validations.FileSize;
import br.com.tcloss.seletivoseplagapi.domain.model.album.ImageType;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;

public record ImageDto(
        @RestForm("file") 
        @NotNull(message = "O arquivo de imagem é obrigatorio")
        @FileImage(message = "O arquivo deve ser uma imagem")
        @FileSize(maxSizeBytes = 3000000,  message = "A imagem não pode ser maior que 3Mb")
        @PartType(MediaType.APPLICATION_OCTET_STREAM) FileUpload file,

        @NotNull(message = "Informe o tipo da imagem")
        @Schema(description = "O tipo da imagem", defaultValue = "COVER")
        @RestForm("type") @PartType(MediaType.TEXT_PLAIN) ImageType type) {

}
