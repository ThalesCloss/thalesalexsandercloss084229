package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.BaseEntity;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.Period;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Lineup extends BaseEntity<UUID> {
    private UUID id;

    private ProperName label;

    private Period duration;

    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        final boolean exists = this.members.stream().anyMatch(presentMember->presentMember.personId().equals(member.personId()));
        if(exists) {
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

    public static Lineup createNew(String label,List<Member> members, LocalDate startDate, LocalDate endDate) {
        final var notification = Notification.create();
        final ProperName properName = notification.tryExecute(() -> new ProperName(label));
        final Period period = notification.tryExecute(() -> new Period(startDate, endDate));
        final Lineup lineup = new Lineup();
        lineup.label = properName;
        lineup.duration = period;
        members.forEach((member)-> notification.tryExecute(() -> lineup.addMember(member)));
        if(!lineup.hasMainArtist()){
            notification.append("A formação deve ter ao menos um artista principal");
        }
        notification.throwIfHasErrors();
        return lineup;
    }
}
