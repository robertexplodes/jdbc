package persistence;

import domain.Bestellung;
import domain.Produkt;

import java.util.List;

public interface BestellungRepository extends Repository<Integer, Bestellung> {

    List<Produkt> findAllProdukteByBestellung(Bestellung bestellung);

}
