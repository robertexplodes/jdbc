package domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
public class Bestellung {

    private Integer bestellungId;
    @NonNull
    private LocalDate bestellDatum;
    @NonNull
    private Kunde kunde;

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
