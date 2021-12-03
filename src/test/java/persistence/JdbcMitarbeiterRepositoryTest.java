package persistence;

import domain.Mitarbeiter;
import domain.Rolle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.supplier.TestConnectionSupplier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcMitarbeiterRepositoryTest {

    private final TestConnectionSupplier connectionSupplier = new TestConnectionSupplier();
    private MitarbeiterRepository mitarbeiterRepository;
    private Connection connection;

    @BeforeEach
    void createRepository() throws SQLException {
        connection = connectionSupplier.getConnection();
        mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void findByName() throws SQLException {
        var mitarbeiter = List.of(
                new Mitarbeiter("thha", "Theo Hahn", Rolle.ANGESTELLTER, 3000.0)
        );
        var sameFirstName = List.of(
                new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0),
                new Mitarbeiter("mama", "Max Musterfrau", Rolle.ANGESTELLTER, 3000.0)
        );

        for (var m : mitarbeiter) {
            mitarbeiterRepository.save(m);
        }
        for (var m : sameFirstName) {
            mitarbeiterRepository.save(m);
        }
        var result = mitarbeiterRepository.findByName("Max");
        assertEquals(sameFirstName.size(), result.size());
    }

    @Test
    void findAll() throws SQLException {
        var mitarbeiter = List.of(
                new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0),
                new Mitarbeiter("thha", "Theo Hahn", Rolle.ANGESTELLTER, 3000.0),
                new Mitarbeiter("flka", "Florian Keller", Rolle.MEISTER, 3500.0)
        );
        for (var m : mitarbeiter) {
            mitarbeiterRepository.save(m);
        }
        var result = mitarbeiterRepository.findAll();
        assertEquals(mitarbeiter.size(), result.size());
    }

    @Test
    void save_works() throws SQLException {
        var mitarbeiter = List.of(
                new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0),
                new Mitarbeiter("mame", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0)
        );
        for (var m : mitarbeiter) {
            mitarbeiterRepository.save(m);
        }
        var all = mitarbeiterRepository.findAll();
        assertEquals(mitarbeiter.size(), all.size());
    }

    @Test
    void save_throws() throws SQLException {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0);
        mitarbeiterRepository.save(mitarbeiter);
        assertThrows(SQLException.class, () -> mitarbeiterRepository.save(mitarbeiter));
    }

    @Test
    void delete() throws SQLException {
        var mitarbeiter = List.of(
                new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0),
                new Mitarbeiter("geri", "Gerd Riesenhuber", Rolle.LEHRLING, 3500)
        );
        var wilLBeDeleted = new Mitarbeiter("mama", "Max Musterfrau", Rolle.ANGESTELLTER, 3000.0);
        for (var m : mitarbeiter) {
            mitarbeiterRepository.save(m);
        }
        mitarbeiterRepository.save(wilLBeDeleted);
        mitarbeiterRepository.delete(wilLBeDeleted);
        var inDatabase = mitarbeiterRepository.findAll();
        assertEquals(mitarbeiter, inDatabase);
    }

    @Test
    void findByPrimaryKey() throws SQLException {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0);
        mitarbeiterRepository.save(mitarbeiter);
        var result = mitarbeiterRepository.findById(mitarbeiter.getNamenskuerzel());
        var acutal = result.orElseThrow();
        assertEquals(mitarbeiter, acutal);
    }

    @Test
    void update_works() throws SQLException {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0);
        mitarbeiterRepository.save(mitarbeiter);
        var newMitarbeiter = new Mitarbeiter("mamu", "Max Maxmusterfrau", Rolle.ANGESTELLTER, 3500.0);
        mitarbeiterRepository.update(newMitarbeiter);
        var result = mitarbeiterRepository.findAll();
        assert result.size() == 1;
        var actual = result.get(0);
        assertEquals(newMitarbeiter.getName(), actual.getName());
    }

    @Test
    void update_throws() throws SQLException {
        var mitarbeiter = new Mitarbeiter("mamu", "Max Mustermann", Rolle.ANGESTELLTER, 3000.0);
        mitarbeiterRepository.save(mitarbeiter);
        var newMitarbeiter = new Mitarbeiter("mama", "Max Maxmusterfrau", Rolle.ANGESTELLTER, 3500.0);
        assertThrows(SQLException.class, () -> mitarbeiterRepository.update(newMitarbeiter));
    }
}