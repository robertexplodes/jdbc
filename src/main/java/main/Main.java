package main;


import domain.*;
import persistence.JdbcBestellungRepository;
import persistence.JdbcKundenRepository;
import persistence.JdbcProduktRepository;
import persistence.KundenRepository;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tischlerunternehmen", "admin","password");
        var kundenRepository = new JdbcKundenRepository(connection);
        var bestellungRepository = new JdbcBestellungRepository(connection);
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
        var produktRepository = new JdbcProduktRepository(connection);
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
    }
}
