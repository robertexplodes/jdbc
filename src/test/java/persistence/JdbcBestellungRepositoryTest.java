package persistence;

import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.supplier.TestConnectionSupplier;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JdbcBestellungRepositoryTest {

    private final TestConnectionSupplier connectionSupplier = new TestConnectionSupplier();
    private KundenRepository kundenRepository;
    private BestellungRepository bestellungRepository;
    private Connection connection;

    @BeforeEach
    void createRepository() throws SQLException {
        connection = connectionSupplier.getConnection();
        kundenRepository = JdbcKundenRepository.getInstance(connection);
        bestellungRepository = JdbcBestellungRepository.getInstance(connection);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void findAllProdukteInBestellung() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        var produkte = List.of(
                new Produkt(1, Holzart.EICHE, "Schreibtisch"),
                new Produkt(2, Holzart.TANNE, "Bett")
        );
        var produktRepository = JdbcProduktRepository.getInstance(connection);
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        bestellungRepository.addProdukt(bestellungen.get(0), produkte.get(0), 1);
        bestellungRepository.addProdukt(bestellungen.get(0), produkte.get(1), 1);
        var found = bestellungRepository.findAllProdukteInBestellung(bestellungen.get(0));
        assert found.keySet().size() == produkte.size();
        var foundList = found.keySet().stream().toList();
        assertEquals(produkte.get(0), foundList.get(0));
    }

    @Test
    void findAllByKunde() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );

        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        var found = bestellungRepository.findAllByKunde(kunden.get(0));
        assertEquals(bestellungen.subList(0, 1), found);
    }

    @Test
    void findAll() throws SQLException{
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );

        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        var found = bestellungRepository.findAll();
        assertEquals(bestellungen, found);
    }

    @Test
    void save_works() throws SQLException{
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        var found = bestellungRepository.findAll();
        assertEquals(bestellungen.size(), found.size());
    }

    @Test
    void delete() throws SQLException{
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        bestellungRepository.delete(bestellungen.get(0));
        var found = bestellungRepository.findAll();
        assertEquals(bestellungen.size()-1, found.size());
    }

    @Test
    void delete_throws() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        assertThrows(SQLException.class, () ->
                bestellungRepository.delete(new Bestellung(404, LocalDate.now(), kunden.get(0), null)));
    }

    @Test
    void update() throws SQLException{
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at"),
                new Kunde(3, "Peter", "peter@peter.com")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        bestellungRepository.update(new Bestellung(2, LocalDate.now(), kunden.get(2), null));
        var found = bestellungRepository.findById(2).orElseThrow();
        assertEquals(kunden.get(2).getEmail(), found.getKunde().getEmail());
    }

    @Test
    void update_throws() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at"),
                new Kunde(3, "Peter", "peter@peter.com")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        assertThrows(SQLException.class, () -> bestellungRepository.delete(new Bestellung(404, LocalDate.now(), kunden.get(0), null)));
    }


    @Test
    void findById() throws SQLException {
        var kunden = List.of(
                new Kunde(1, "Max", "max@mustermann.at"),
                new Kunde(2, "Hans", "test@test.at"),
                new Kunde(3, "Peter", "peter@peter.com")
        );
        var bestellungen = List.of(
                new Bestellung(1, LocalDate.now(), kunden.get(0), null),
                new Bestellung(2, LocalDate.now(), kunden.get(1), null)
        );
        for (var kunde : kunden) {
            kundenRepository.save(kunde);
        }
        for (var bestellung : bestellungen) {
            bestellungRepository.save(bestellung);
        }
        var found = bestellungRepository.findById(2).orElseThrow();
        assertEquals(bestellungen.get(1).getKunde(), found.getKunde());
    }
}