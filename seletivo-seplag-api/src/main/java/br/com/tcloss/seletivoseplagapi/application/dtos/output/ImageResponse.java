package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.net.URI;

public record ImageResponse(
    URI url,
    String type
) {

}
