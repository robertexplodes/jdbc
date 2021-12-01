package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MitarbeiterTest {
    @Test
    void has_correct_role_with_string_constructor() {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", "MEISTER", 3000);
        assertEquals(Rolle.MEISTER, mitarbeiter.getRolle());
    }

    @Test
    void constructor_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Mitarbeiter("mamu", "Max Mustermann", "FAILS", 3000));
    }

    @Test
    void equal() {
        var m1 = new Mitarbeiter("mamu", "Max Mustermann", "MEISTER", 3000);
        var m2 = new Mitarbeiter("mamu", "test", "MEISTER", 3000);
        assertEquals(m1, m2);
    }

    @Test
    void not_equal() {
        var m1 = new Mitarbeiter("mamu", "Max Mustermann", "MEISTER", 3000);
        var m2 = new Mitarbeiter("mama", "Max Mustermann", "MEISTER", 3000);
        assertNotEquals(m1, m2);
    }

    @Test
    void correct_Rollen_property() {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", "MEISTER", 3000);
        var rp = mitarbeiter.rollenProperty();
        assertEquals("Meister", rp.get());
    }
}