package persistence;

import domain.Bestellung;
import domain.Kunde;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.supplier.TestConnectionSupplier;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcKundenRepositoryTest {

    private final TestConnectionSupplier connectionSupplier = new TestConnectionSupplier();
    private KundenRepository kundenRepository;
    private Connection connection;

    @BeforeEach
    void createRepository() throws SQLException {
        connection = connectionSupplier.getConnection();
        kundenRepository = new JdbcKundenRepository(connection);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void findAllBestellungen() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );

        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        var bestellungenRepository = new JdbcBestellungRepository(connection);
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungenRepository.save(bestellung);
        }
        var found = kundenRepository.findAllBestellungen(kunden.get(0));
        assertEquals(bestellungen.subList(0, 1), found);
    }

    @Test
    void findAll() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "test@email.com"),
                new Kunde(1, "Maxi", "test@email.com")
        );
        for (Kunde kunde : kunden) {
            kundenRepository.save(kunde);
        }
        var result = kundenRepository.findAll();
        assertEquals(kunden.size(), result.size());
    }

    @Test
    void save() throws SQLException {
        var kunde = new Kunde(1, "Max", "test@email.com");
        kundenRepository.save(kunde);
        var found = kundenRepository.findById(1).orElseThrow();
        assertEquals(kunde, found);
    }

    @Test
    void delete() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "test@email.com"),
                new Kunde(2, "Maxi", "test@email.com")
        );
        for (Kunde kunde : kunden) {
            kundenRepository.save(kunde);
        }
        kundenRepository.delete(kunden.get(0));
        var result = kundenRepository.findAll();
        assert result.size() == 1;
        assertEquals(kunden.get(1), result.get(0));
    }

    @Test
    void update() throws SQLException {
        var kunde = new Kunde(1, "Max", "test@email.com");
        kundenRepository.save(kunde);
        var updated = new Kunde(1, "manuel", "manuel@email.com");
        kundenRepository.update(updated);
        var found = kundenRepository.findById(1).orElseThrow();

        assertEquals(updated.getEmail(), found.getEmail());
    }

    @Test
    void findById() throws SQLException{
        var kunde = new Kunde(1, "Max", "test@email.com");
        kundenRepository.save(kunde);
        var found = kundenRepository.findById(1).orElseThrow();
        assertEquals(kunde, found);
    }
}