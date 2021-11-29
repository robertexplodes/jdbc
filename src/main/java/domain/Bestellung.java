package domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
public class Bestellung {

    private Integer bestellungId;
    @NonNull
    private LocalDate bestellDatum;
    @NonNull
    private Kunde kunde;

    private Mitarbeiter mitarbeiter;
}
