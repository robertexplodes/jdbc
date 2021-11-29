package persistence;

import domain.Holzart;
import domain.Produkt;

import java.sql.SQLException;
import java.util.List;

public interface ProduktRepository extends Repository<Integer, Produkt> {

    List<Produkt> findAllByHolzart(Holzart holzart) throws SQLException;

}
