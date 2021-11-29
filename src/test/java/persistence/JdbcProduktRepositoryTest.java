package persistence;

import domain.Holzart;
import domain.Produkt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.supplier.TestConnectionSupplier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProduktRepositoryTest {

    private final TestConnectionSupplier connectionSupplier = new TestConnectionSupplier();
    private ProduktRepository produktRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = connectionSupplier.getConnection();
        produktRepository = new JdbcProduktRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void findAll() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        assertEquals(produkte, produktRepository.findAll());
    }

    @Test
    void save_works() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        assertEquals(produkte.size(), produktRepository.findAll().size());
    }

    // TODO: findByHolzart
    @Test
    void update_works() throws SQLException {
        var produkt = new Produkt(1, Holzart.BIRKE, "Tisch");
        produktRepository.save(produkt);
        var produkt2 = new Produkt(1, Holzart.EICHE, "Stuhl");
        produktRepository.update(produkt2);
        var inDatabase = produktRepository.findAll();
        assert inDatabase.size() == 1;
        assertEquals(produkt2.getProduktart(), inDatabase.get(0).getProduktart());
    }

    @Test
    void update_throws() throws SQLException {
        var produkt = new Produkt(1, Holzart.BIRKE, "Tisch");
        produktRepository.save(produkt);
        var produkt2 = new Produkt(2, Holzart.EICHE, "Stuhl");
        assertThrows(SQLException.class, () -> produktRepository.update(produkt2));
    }

    @Test
    void delete_works() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl")
        );
        var willBeDeleted = new Produkt(3, Holzart.EICHE, "Kasten");
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        produktRepository.save(willBeDeleted);
        produktRepository.delete(willBeDeleted);
        assertEquals(produkte, produktRepository.findAll());
    }

    @Test
    void delete_throws() throws SQLException {
        var produkt = new Produkt(1, Holzart.BIRKE, "Tisch");
        assertThrows(SQLException.class, () -> produktRepository.delete(produkt));
    }

    @Test
    void findById_is_present() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        var actual = produktRepository.findById(1).orElseThrow();
        assertEquals(produkte.get(0), actual);
    }

    @Test
    void findById_is_empty() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        assertTrue(produktRepository.findById(4).isEmpty());
    }

    @Test
    void findByHolzart_is_empty() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        assertTrue(produktRepository.findAllByHolzart(Holzart.KIRSCHE).isEmpty());
    }

    @Test
    void findByHolzart_contains_exactly() throws SQLException {
        var produkte = List.of(
                new Produkt(1, Holzart.EICHE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for (var produkt : produkte) {
            produktRepository.save(produkt);
        }
        var actual = produktRepository.findAllByHolzart(Holzart.EICHE);
        assertEquals(produkte, actual);
    }
}