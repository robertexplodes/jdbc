package persistence;

import domain.Bestellung;
import domain.Kunde;

import java.sql.SQLException;
import java.util.List;

public interface KundenRepository extends Repository<Integer, Kunde> {

    List<Bestellung> findAllBestellungen(Kunde kunde) throws SQLException;

    List<Kunde> findAllByStringInNameOrEmail(String value) throws SQLException;
}
