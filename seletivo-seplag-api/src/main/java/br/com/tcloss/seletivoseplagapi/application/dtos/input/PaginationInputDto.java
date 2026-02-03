package br.com.tcloss.seletivoseplagapi.application.dtos.input;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.ws.rs.QueryParam;

public record PaginationInputDto(
    @QueryParam("page")
    @Schema(description = "Página de resultados", defaultValue = "1", comment = "Página atual")
    int page,
    @QueryParam("limit")
    @Schema(description = "Limite de itens por página", defaultValue = "10")
    int limit
) {

    public int calculateTotalPages(long totalItems){
        return (int) Math.ceilDiv(totalItems, limit);
    }

    public int getCurrentPageSafely(int totalPages){
        return Math.min(totalPages, page);
    }

}
