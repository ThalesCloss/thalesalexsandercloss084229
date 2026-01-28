package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.BaseEntity;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.Period;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "lineups")
@Entity
public class Lineup extends BaseEntity<UUID> {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "label", nullable = false, length = 200))
    private ProperName label;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date"))
    })
    private Period duration;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lineup_members", joinColumns = @JoinColumn(name = "lineup_id"))
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        final boolean exists = this.members.stream()
                .anyMatch(presentMember -> presentMember.personId().equals(member.personId()));
        if (exists) {
            throw new IllegalArgumentException("Membro %s já existe na formação".formatted(member.personId()));
        }
        this.members.add(member);
    }

    public boolean hasMainArtist() {
        return this.members.stream().anyMatch(Member::isMainArtist);
    }

    public void finishLineup(LocalDate endDate) {
        this.duration = new Period(this.duration.startDate(), endDate);
    }

    public static Lineup createNew(String label, List<Member> members, LocalDate startDate, LocalDate endDate) {
        final var notification = Notification.create();
        final ProperName properName = notification.tryExecute(() -> new ProperName(label));
        final Period period = notification.tryExecute(() -> new Period(startDate, endDate));
        final Lineup lineup = new Lineup();
        lineup.label = properName;
        lineup.duration = period;
        members.forEach((member) -> notification.tryExecute(() -> lineup.addMember(member)));
        if (!lineup.hasMainArtist()) {
            notification.append("A formação deve ter ao menos um artista principal");
        }
        notification.throwIfHasErrors();
        return lineup;
    }
}
