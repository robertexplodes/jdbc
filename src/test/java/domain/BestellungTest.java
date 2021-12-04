package domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BestellungTest {

    @Test
    void equal_same_id() {
        var b1 = new Bestellung(1, LocalDate.now(),
                new Kunde(1, "Hansi", "Hansi@hinterseer.com"), null);
        var b2 = new Bestellung(1, LocalDate.now(),
                new Kunde(1, "Josef", "josef@hinterseer.com"), null);
        assertEquals(b1, b2);
    }

    @Test
    void not_equal_different_id() {
        var b1 = new Bestellung(1, LocalDate.now(),
                new Kunde(1, "Hansi", "Hansi@hinterseer.com"), null);
        var b2 = new Bestellung(2, LocalDate.now(),
                new Kunde(1, "Josef", "josef@mayer.at"), null);
        assertNotEquals(b1, b2);
    }

    @Test
    void correct_compare_to_implementation() {
        var bestellungen = new ArrayList<>(List.of(
                new Bestellung(2, LocalDate.now(),
                        new Kunde(1, "Hansi", "Hansi@hinterseer.com"), null),
                new Bestellung(1, LocalDate.now(),
                        new Kunde(2, "Josef", "josef@mayer.at"), null)
        ));
        Collections.sort(bestellungen);
        assertEquals(1, bestellungen.get(0).getBestellungId());
    }
}