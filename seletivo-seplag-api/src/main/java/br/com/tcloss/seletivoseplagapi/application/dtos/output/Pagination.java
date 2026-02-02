package br.com.tcloss.seletivoseplagapi.application.dtos.output;

public record Pagination(
    int totalPages,
    int currentPage,
    int itemsPerPage,
    long totalIems
) {

}
