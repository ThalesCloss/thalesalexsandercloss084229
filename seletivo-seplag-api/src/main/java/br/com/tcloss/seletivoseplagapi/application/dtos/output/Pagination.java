package br.com.tcloss.seletivoseplagapi.application.dtos.output;

public record Pagination(
    Integer totalPages,
    Integer currentPage,
    Integer itemsPerPage,
    Integer totalIems
) {

}
