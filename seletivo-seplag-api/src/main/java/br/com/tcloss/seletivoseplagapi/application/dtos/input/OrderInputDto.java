package br.com.tcloss.seletivoseplagapi.application.dtos.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import jakarta.ws.rs.QueryParam;

public record OrderInputDto(
        @Parameter(description = "Ordenação múltipla. Formato: propriedade,direção"

        ) @QueryParam("orderBy") List<String> orderBy) {

    public enum OrderDirection {
        ASC,
        DESC
    }

    public Map<String, OrderDirection> getOreders() {
        Map<String, OrderDirection> orders = new HashMap<String, OrderDirection>();
        for (String param : orderBy) {
            var parts = param.split(",");
            var order = parts.length == 1 ? "asc" : parts[1];
            orders.put(parts[0].trim(), OrderDirection.valueOf(order.toUpperCase()));
        }
        return orders;
    }
}
