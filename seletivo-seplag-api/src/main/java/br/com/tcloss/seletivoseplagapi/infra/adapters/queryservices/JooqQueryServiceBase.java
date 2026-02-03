package br.com.tcloss.seletivoseplagapi.infra.adapters.queryservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SortField;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;

public abstract class JooqQueryServiceBase {


    protected abstract Map<String, Field<?>> mapOrdersAllowed();
    protected abstract SortField<?> defaultOrder();

    protected abstract long countTotal(Condition where);
    

    protected List<SortField<?>> orderBy(OrderInputDto orderInputDto) {
        Map<String, Field<?>> orderAllowed = mapOrdersAllowed();
        List<SortField<?>> orderBy = new ArrayList<>();
        var orders = orderInputDto.getOreders();
        orderAllowed.forEach((fieldName, field) -> {
            if (orders.containsKey(fieldName)) {
                orderBy.add(
                        orders.get(fieldName).equals(OrderInputDto.OrderDirection.ASC) ? field.asc().nullsLast()
                                : field.desc().nullsLast());
            }
        });
        if (orderBy.isEmpty()) {
            orderBy.add(defaultOrder());
        }
        return orderBy;
    }
    protected int convertCurrentPageToOffset(int currentPage, int limit){
        return (currentPage - 1) * limit;
    }
}
