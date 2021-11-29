package persistence;

import domain.Bestellung;
import domain.Kunde;

import java.util.List;

public interface KundenRepository extends Repository<Integer, Kunde> {

    List<Bestellung> findAllBestellungen(Kunde kunde);

}
