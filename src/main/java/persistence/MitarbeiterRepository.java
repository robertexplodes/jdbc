package persistence;

import domain.Mitarbeiter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MitarbeiterRepository extends Repository<String, Mitarbeiter> {

    List<Mitarbeiter> findByName(String name) throws SQLException;

}
