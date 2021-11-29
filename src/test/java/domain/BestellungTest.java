package domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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



}