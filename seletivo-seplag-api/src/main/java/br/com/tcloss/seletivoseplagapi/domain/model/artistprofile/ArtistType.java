package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;

public enum ArtistType implements ArtistTypePolicy {
    SOLO {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            if (newLineup.getMembers().size() != 1) {
                throw new DomainException("A formação de um artista solo deve conter exatamente um membro.");
            }
            if(!artist.getLineups().isEmpty()){
                throw new DomainException("Artistas solo não podem alterar sua formação após a criação inicial.");
            }
        }
    },
    BAND {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            validateMinimumMembers( newLineup, 4);
        }
    },
    ORCHESTRA {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            validateMinimumMembers( newLineup, 10);
        }
    },
    CHOIR {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            validateMinimumMembers( newLineup, 8);
        }
    },
    DUO {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            if (newLineup.getMembers().size() != 2) {
                throw new DomainException("A formação de um artista duo deve conter exatamente dois membros.");
            }
        }
    },
    ENSEMBLE {
        @Override
        public void validate(ArtistProfile artist, Lineup newLineup) {
            validateMinimumMembers( newLineup, 4);
        }
    };
    private static void validateMinimumMembers(Lineup newLineup, int minMembers) {
        if (newLineup.getMembers().size() < minMembers) {
            throw new DomainException("A formação deve conter pelo menos " + minMembers + " membros.");
        }
    }
}
