package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.util.List;

public record MultipleItemsResult<T>(
        List<T> data,
        Pagination pagination) {

}
