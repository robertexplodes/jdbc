package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProduktTest {

    @Test
    void has_correct_holzart_with_string_constructor() {
        var produkt = new Produkt(1, "EICHE", "Tisch");
        assertEquals(Holzart.EICHE, produkt.getHolzart());
    }

    @Test
    void constructor_throws() {
        assertThrows(IllegalArgumentException.class,() -> new Produkt(1, "INCORRECT", "Tisch"));
    }


}