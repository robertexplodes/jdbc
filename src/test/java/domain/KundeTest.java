package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KundeTest {
    @Test
    void equal_same_id() {
        var k1 = new Kunde(1, "Hansi", "hansi@hinterseer.com");
        var k2 = new Kunde(1, "Andreas", "andreas@gablier.com");
        assertEquals(k1, k2);
    }
    @Test
    void not_equal_different_id() {
        var k1 = new Kunde(1, "Hansi", "hansi@hinterseer.com");
        var k2 = new Kunde(2, "Andreas", "andreas@gablier.com");
        assertNotEquals(k1, k2);
    }

    @Test
    void correct_email() {
        assertDoesNotThrow(() -> new Kunde("Hansi", "adsf@asdf.com"));
    }

    @Test
    void incorrect_email() {
        assertThrows(IllegalArgumentException.class, () -> new Kunde("Hansi", "adsf"));
    }
}