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
    void findAllByHolzart() {
    }

    @Test
    void findAll() throws SQLException{
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for(var produkt : produkte){
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
        for(var produkt : produkte){
            produktRepository.save(produkt);
        }
        assertEquals(produkte.size(), produktRepository.findAll().size());
    }

    @Test
    void save_throws() throws SQLException{
        var p1 = new Produkt(1, Holzart.BIRKE, "Tisch");
        var p2 = new Produkt(1, Holzart.EICHE, "Stuhl");
        produktRepository.save(p1);
        assertThrows(SQLException.class, () -> produktRepository.save(p2));
    }

    @Test
    void delete_works() throws SQLException{
        var produkte = List.of(
                new Produkt(1, Holzart.BIRKE, "Tisch"),
                new Produkt(2, Holzart.EICHE, "Stuhl"),
                new Produkt(3, Holzart.EICHE, "Kasten")
        );
        for(var produkt : produkte){
            produktRepository.save(produkt);
        }
        produktRepository.delete(produkte.get(0));
        assertEquals(List.of(produkte.get(1), produkte.get(2)), produktRepository.findAll());
    }

    @Test
    void findByPrimaryKey() {
    }
}