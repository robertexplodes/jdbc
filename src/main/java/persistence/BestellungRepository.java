package persistence;

import domain.Bestellung;
import domain.Kunde;
import domain.Produkt;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BestellungRepository extends Repository<Integer, Bestellung> {

    Map<Produkt, Integer> findAllProdukteInBestellung(Bestellung bestellung) throws SQLException;

    List<Bestellung> findAllByKunde(Kunde kunde) throws SQLException;

    void addProdukt(Bestellung bestellung, Produkt produkt, int menge) throws SQLException;
}
