package br.com.tcloss.seletivoseplagapi.infra.adapters.queryservices;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Table;
import org.jooq.impl.DSL;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.Pagination;
import br.com.tcloss.seletivoseplagapi.application.ports.AlbumQueryService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ALBUMS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ALBUM_IMAGES;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ALBUM_TRACKS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ALBUM_TRACK_GUESTS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.ARTIST_PROFILES;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.LINEUP_MEMBERS;
import static br.com.tcloss.seletivoseplagapi.jooq.Tables.PERSONS;
import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class JooqAlbumQueryService implements AlbumQueryService {
    private final DSLContext dsl;

    @Override
    public MultipleItemsResult<AlbumSummaryDataProjection> searchAlbums(AlbumSearchDto filter,
            PaginationInputDto pagination) {
        final Condition where = applyFilter(filter);
        final long countTotal = countTotal(where);
        final int offset = (pagination.page() - 1) * pagination.page();
        if (offset > countTotal) {
            return null;
        }
        final var result = search(where, pagination.limit(), offset);
        final int pages = (int) Math.ceilDiv(countTotal, pagination.limit());
        return new MultipleItemsResult<>(
                result, new Pagination(pages, pagination.page(), pagination.limit(), countTotal));
    }

    private long countTotal(Condition where) {
        return dsl.selectCount()
                .from(ALBUMS)
                .join(ARTIST_PROFILES).on(ALBUMS.ARTIST_PROFILE_ID.eq(ARTIST_PROFILES.ID))
                .where(where).fetchSingleInto(Long.class);
    }

    private List<AlbumSummaryDataProjection> search(Condition where, int limit, int offset) {
        var imgTable = queryLateralSingleImage();
        return dsl.select(
                ALBUMS.ID.as("albumId"),
                ALBUMS.TITLE,
                ARTIST_PROFILES.ID.as("artistId"),
                ARTIST_PROFILES.STAGE_NAME.as("artistName"),
                ALBUMS.START_DATE.as("releaseDate"),
                DSL.field(
                        DSL.selectCount()
                                .from(ALBUM_TRACKS)
                                .where(ALBUM_TRACKS.ALBUM_ID
                                        .eq(ALBUMS.ID))),
                DSL.row(
                        imgTable.field(ALBUM_IMAGES.IDENTIFIER),
                        imgTable.field(ALBUM_IMAGES.TYPE))
                        .mapping(ImageDataProjection::new)
                        .as("image"))
                .from(ALBUMS)
                .join(ARTIST_PROFILES).on(ALBUMS.ARTIST_PROFILE_ID
                        .eq(ARTIST_PROFILES.ID))
                .leftJoin(imgTable).on(DSL.trueCondition())
                .where(where)
                .limit(limit)
                .offset(offset)
                .fetchInto(AlbumSummaryDataProjection.class);
    }

    private Table<Record2<String, String>> queryLateralSingleImage() {
        return DSL.lateral(
                DSL.select(ALBUM_IMAGES.IDENTIFIER, ALBUM_IMAGES.TYPE)
                        .from(ALBUM_IMAGES)
                        .where(ALBUM_IMAGES.ALBUM_ID.eq(ALBUMS.ID))
                        .orderBy(DSL.field("array_position(ARRAY['COVER', 'DISC', 'BOOKLET']::varchar[], {0})",
                                Integer.class, ALBUM_IMAGES.TYPE).asc())
                        .limit(1))
                .as("img");
    }

    private Condition applyFilter(AlbumSearchDto filter) {
        Condition where = DSL.noCondition();
        if (filter.startReleaseDate() != null) {
            where = where.and(ALBUMS.START_DATE.ge(filter.startReleaseDate()));
        }
        if (filter.endReleaseDate() != null) {
            where = where.and(ALBUMS.START_DATE.le(filter.endReleaseDate()));
        }
        if (filter.albumTitle() != null && !filter.albumTitle().isEmpty()) {
            where = where.and(ALBUMS.TITLE.containsIgnoreCase(filter.albumTitle()));
        }
        if (filter.searchSpecificArtists()) {
            where = where.and(ALBUMS.ARTIST_PROFILE_ID.in(filter.specificArtists()));
        }
        if (filter.artistName() != null && !filter.artistName().isEmpty()) {
            Condition artistWhere = ARTIST_PROFILES.STAGE_NAME.containsIgnoreCase(filter.artistName());
            if (filter.searchInMembers()) {
                var artistMember = DSL.selectOne()
                        .from(LINEUP_MEMBERS)
                        .join(PERSONS).on(PERSONS.ID.eq(LINEUP_MEMBERS.PERSON_ID))
                        .where(ALBUMS.ARTIST_PROFILE_LINEUP_ID.eq(LINEUP_MEMBERS.LINEUP_ID))
                        .and(PERSONS.NAME.containsIgnoreCase(filter.artistName()));

                artistWhere = artistWhere.or(DSL.exists(artistMember));
            }
            if (filter.searchInGuests()) {
                var artistGuest = DSL.selectOne()
                        .from(ALBUM_TRACK_GUESTS)
                        .join(ALBUM_TRACKS).on(ALBUM_TRACKS.ID.eq(ALBUM_TRACK_GUESTS.TRACK_ID))
                        .join(ALBUMS).on(ALBUMS.ID.eq(ALBUM_TRACKS.ALBUM_ID))
                        .join(PERSONS).on(PERSONS.ID.eq(ALBUM_TRACK_GUESTS.PERSON_ID))
                        .where(
                                DSL.or(ALBUM_TRACK_GUESTS.CREDIT_NAME.containsIgnoreCase(filter.artistName()),
                                        PERSONS.NAME.containsIgnoreCase(filter.artistName())));

                artistWhere = artistWhere.or(DSL.exists(artistGuest));
            }
            where = where.and(artistWhere);
        }
        return where;
    }

}
