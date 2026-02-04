package br.com.tcloss.seletivoseplagapi.infra.adapters.queryservices;

import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ARTIST_PROFILES;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.LINEUPS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.LINEUP_MEMBERS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.PERSONS;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Records;
import org.jooq.SelectConditionStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.Pagination;
import br.com.tcloss.seletivoseplagapi.application.ports.ArtistProfileQueryService;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchArtistProfilesQuery;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse.ArtistProfileMember;

@ApplicationScoped
@AllArgsConstructor
public class JooqArtistProfileQueryService extends JooqQueryServiceBase implements ArtistProfileQueryService {

    private final DSLContext dsl;

    @Override
    public MultipleItemsResult<ArtistProfileResponse> searchArtistProfiles(
            SearchArtistProfilesQuery searchArtistProfilesQuery) {
        final var where = applyFilter(searchArtistProfilesQuery.artistProfileSearch());
        final var countTotal = countTotal(where);
        final var totalPages = searchArtistProfilesQuery.paginationInputDto().calculateTotalPages(countTotal);
        final var currentPage = searchArtistProfilesQuery.paginationInputDto().getCurrentPageSafely(totalPages);
        final var limit = searchArtistProfilesQuery.paginationInputDto().limit();
        return new MultipleItemsResult<>(
                countTotal > 0
                        ? search(where, limit,
                                convertCurrentPageToOffset(currentPage, limit),
                                searchArtistProfilesQuery.orderInputDto())
                        : List.of(),
                new Pagination(totalPages, currentPage, searchArtistProfilesQuery.paginationInputDto().limit(),
                        countTotal)

        );
    }

    @Override
    protected Map<String, Field<?>> mapOrdersAllowed() {
        return Map.of(
                "stage_name", ARTIST_PROFILES.STAGE_NAME,
                "stageName", ARTIST_PROFILES.STAGE_NAME);
    }

    @Override
    protected SortField<?> defaultOrder() {
        return ARTIST_PROFILES.STAGE_NAME.asc();
    }

    @Override
    protected long countTotal(Condition where) {
        return dsl.selectCount().from(ARTIST_PROFILES).where(where).fetchSingleInto(Long.class);
    }

    private Condition applyFilter(ArtistProfileSearchDto artistProfileSearchDto) {
        var where = DSL.noCondition();
        if (artistProfileSearchDto.stageName() != null && !artistProfileSearchDto.stageName().isEmpty()) {
            where = where.and(ARTIST_PROFILES.STAGE_NAME.containsIgnoreCase(artistProfileSearchDto.stageName()));
        }
        if (artistProfileSearchDto.memberName() != null && !artistProfileSearchDto.memberName().isEmpty()) {
            where = where.andExists(
                    DSL.selectOne()
                            .from(LINEUP_MEMBERS)
                            .join(LINEUPS).on(LINEUPS.ID.eq(LINEUP_MEMBERS.LINEUP_ID))
                            .join(PERSONS).on(LINEUP_MEMBERS.PERSON_ID.eq(PERSONS.ID))
                            .where(LINEUPS.ARTIST_PROFILE_ID.eq(ARTIST_PROFILES.ID))
                            .and(PERSONS.NAME.containsIgnoreCase(artistProfileSearchDto.memberName())));
        }
        return where;
    }

    private List<ArtistProfileResponse> search(Condition where, int limit, int offset,
            OrderInputDto orderInputDto) {

        return dsl.select(
                ARTIST_PROFILES.ID.as("artistProfileId"),
                ARTIST_PROFILES.STAGE_NAME,
                ARTIST_PROFILES.BIOGRAPHY,
                ARTIST_PROFILES.ARTIST_TYPE.convertFrom(val -> ArtistType.valueOf(val)),
                DSL.field(
                        DSL.selectCount()
                                .from(LINEUPS)
                                .where(LINEUPS.ARTIST_PROFILE_ID.eq(ARTIST_PROFILES.ID))),
                DSL.multiset(
                        queryLastLineup())
                        .convertFrom(v -> v.map(Records.mapping(ArtistProfileMember::new)))
                        .as("lastLineup"))
                .from(ARTIST_PROFILES)

                .where(where)
                .orderBy(orderBy(orderInputDto))
                .limit(limit)
                .offset(offset)
                .fetchInto(ArtistProfileResponse.class);

    }

    private SelectConditionStep<Record4<UUID, String, Boolean, Boolean>> queryLastLineup() {

        return DSL.select(
                LINEUP_MEMBERS.PERSON_ID,
                PERSONS.NAME.as("personName"),
                LINEUP_MEMBERS.IS_MAIN_ARTIST,
                LINEUP_MEMBERS.IS_FOUNDER)
                .from(LINEUP_MEMBERS)
                .join(PERSONS).on(LINEUP_MEMBERS.PERSON_ID.eq(PERSONS.ID))
                .where(LINEUP_MEMBERS.LINEUP_ID.eq(
                        DSL.select(LINEUPS.ID)
                                .from(LINEUPS)
                                .where(LINEUPS.ARTIST_PROFILE_ID.eq(ARTIST_PROFILES.ID))
                                .orderBy(LINEUPS.START_DATE.desc())
                                .limit(1)));

    }

}
