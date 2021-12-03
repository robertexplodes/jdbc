package domain;

import domain.interfaces.Persitable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

import java.time.LocalDate;
import java.util.Objects;
@Getter
@AllArgsConstructor
public class Bestellung implements Persitable {

    @With
    private Integer bestellungId;

    @NonNull
    private LocalDate bestellDatum;

    private Kunde kunde; // can be null, when client gets deleted

    private Mitarbeiter mitarbeiter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bestellung that = (Bestellung) o;
        return Objects.equals(bestellungId, that.bestellungId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bestellungId);
    }
}
