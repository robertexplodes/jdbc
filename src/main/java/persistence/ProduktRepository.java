package persistence;

import domain.Holzart;
import domain.Produkt;

import java.sql.SQLException;
import java.util.List;

public interface ProduktRepository extends Repository<Integer, Produkt> {

    /**
     * Finds all entities which contain the given value in their Holzart or Produktart
     * @param value
     */
    List<Produkt> findAllByString(String value) throws SQLException;
}
