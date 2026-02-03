package br.com.tcloss.seletivoseplagapi.infra.adapters.queryservices;

import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ALBUMS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.PERSONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PersonSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.Pagination;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.PersonResponse;
import br.com.tcloss.seletivoseplagapi.application.ports.PersonQueryService;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchPersonsQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class JooqPersonQueryService extends JooqQueryServiceBase implements PersonQueryService {
    private final DSLContext dsl;

    @Override
    public MultipleItemsResult<PersonResponse> searchPersons(SearchPersonsQuery searchPersonsQuery) {
        final var where = applyFilter(searchPersonsQuery.query());
        final var countTotal = countTotal(where);
        final var totalPages = searchPersonsQuery.pagination().calculateTotalPages(countTotal);
        final var currentPage = searchPersonsQuery.pagination().getCurrentPageSafely(totalPages);

        return new MultipleItemsResult<PersonResponse>(
                countTotal > 0
                        ? search(where,
                                searchPersonsQuery.pagination().limit(),
                                convertCurrentPageToOffset(currentPage, searchPersonsQuery.pagination().limit()),
                                searchPersonsQuery.order())
                        : List.of(),
                new Pagination(totalPages, currentPage, searchPersonsQuery.pagination().limit(), currentPage));

    }

    private List<PersonResponse> search(Condition where, int limit, int offset, OrderInputDto orderInputDto) {
        return dsl.select(
                PERSONS.ID,
                PERSONS.NAME,
                PERSONS.BIRTH_DATE)
                .from(PERSONS)
                .where(where)
                .orderBy(orderBy(orderInputDto))
                .limit(limit)
                .offset(offset)
                .fetchInto(PersonResponse.class);
    }

    private Condition applyFilter(PersonSearchDto personSearchDto) {
        var where = DSL.noCondition();
        if (personSearchDto.name() != null && !personSearchDto.name().isEmpty()) {
            where = where.and(PERSONS.NAME.containsIgnoreCase(personSearchDto.name()));
        }
        if (personSearchDto.endBirthDate() != null) {
            where = where.and(PERSONS.BIRTH_DATE.le(personSearchDto.endBirthDate()));
        }
        if (personSearchDto.startBirthDate() != null) {
            where = where.and(PERSONS.BIRTH_DATE.ge(personSearchDto.startBirthDate()));
        }
        return where;
    }

    @Override
    protected long countTotal(Condition where) {
        return dsl.selectCount()
                .from(PERSONS)
                .where(where)
                .fetchSingleInto(Long.class);
    }

    @Override
    protected Map<String, Field<?>> mapOrdersAllowed() {
        return Map.of(
                "name", PERSONS.NAME,
                "birth_date", PERSONS.BIRTH_DATE,
                "birthDate", PERSONS.BIRTH_DATE);
    }

    @Override
    protected SortField<?> defaultOrder() {
        return PERSONS.BIRTH_DATE.desc();
    }

}
