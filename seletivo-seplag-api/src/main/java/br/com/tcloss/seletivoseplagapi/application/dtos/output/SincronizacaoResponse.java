package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.util.List;

import br.com.tcloss.seletivoseplagapi.domain.model.regional.Regional;

public record SincronizacaoResponse(
                List<Regional> novas,
                List<Regional> removidas,
                List<Regional> alteradas

) {
      }
