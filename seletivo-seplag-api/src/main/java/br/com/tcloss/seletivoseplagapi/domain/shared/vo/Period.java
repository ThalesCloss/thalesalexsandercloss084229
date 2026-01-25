package br.com.tcloss.seletivoseplagapi.domain.shared.vo;

import java.time.LocalDate;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;

public record Period(
        LocalDate startDate,
        LocalDate endDate) implements ValueObject {

    public Period {
        if (startDate == null) {
            throw new IllegalArgumentException("Data de início é obrigatória");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Data final não pode ser anterior à inicial");
        }
    }

    public boolean contains(LocalDate referenceDate) {
        if (referenceDate.isBefore(startDate)) {
            return false;
        }
        return endDate == null || !referenceDate.isAfter(endDate);
    }
}
