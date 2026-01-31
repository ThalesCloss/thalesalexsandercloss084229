package br.com.tcloss.seletivoseplagapi.infra;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DBCleaner {
    private static final List<String> TABLES = List.of(
            "album_images",
            "album_track_guests",
            "album_tracks",
            "albums",
            "lineup_members",
            "lineups",
            "artist_profiles",
            "composition_authors",
            "compositions",
            "persons");

    @Inject
    EntityManager em;

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void truncateAll() {
        em.createNativeQuery("TRUNCATE TABLE "+String.join(", ", TABLES)+ " CASCADE").executeUpdate();
    }

}
