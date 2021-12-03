package persistence;

import domain.Bestellung;
import domain.Kunde;
import domain.Produkt;
import lombok.SneakyThrows;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public final class JdbcBestellungRepository implements BestellungRepository {

    private static JdbcBestellungRepository instance = null;
    private final Connection connection;

    private JdbcBestellungRepository(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public static JdbcBestellungRepository getInstance(Connection connection) {
        if (instance == null || instance.connection.isClosed()) {
            instance = new JdbcBestellungRepository(connection);
        }
        return instance;
    }

    private Optional<Bestellung> bestellungOfResultSet(ResultSet resultSet) throws SQLException {
        var bestellnummer = resultSet.getInt("bestellnummer");
        var bestelldatum = resultSet.getDate("bestelldatum");
        var kundenID = resultSet.getInt("kunde");
        var mitarbeiterID = resultSet.getString("mitarbeiter");

        var mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);
        var kundeRepository = JdbcKundenRepository.getInstance(connection);
        var mitabeiter = mitarbeiterRepository.findById(mitarbeiterID).orElse(null);
        var kunde = kundeRepository.findById(kundenID).orElse(null);
        return Optional.of(new Bestellung(bestellnummer, bestelldatum.toLocalDate(), kunde, mitabeiter));
    }

    @Override
    public Map<Produkt, Integer> findAllProdukteInBestellung(Bestellung bestellung) throws SQLException {
        var sql = """
                SELECT produkttyp, amount
                from bestellungsinhalt
                where bestellnummer = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bestellung.getBestellungId());
            var quantity = new HashMap<Produkt, Integer>();
            var resultSet = statement.executeQuery();
            var produktRepository = JdbcProduktRepository.getInstance(connection);
            while (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                var produkt = produktRepository.findById(resultSet.getInt("produkttyp"));
                produkt.ifPresent(value -> quantity.put(value, amount));
            }
            return quantity;
        }
    }

    @Override
    public List<Bestellung> findAllByKunde(Kunde kunde) throws SQLException {
        var sql = """
                SELECT bestellnummer, bestelldatum, kunde, mitarbeiter
                FROM bestellungen
                WHERE kunde = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, kunde.getId());
            var resultSet = statement.executeQuery();
            var found = new ArrayList<Bestellung>();
            while (resultSet.next()) {
                bestellungOfResultSet(resultSet).ifPresent(found::add);
            }
            return found;
        }
    }

    @Override
    public void addProdukt(Bestellung bestellung, Produkt produkt, int menge) throws SQLException {
        var sql = """
                INSERT INTO bestellungsinhalt (bestellnummer, produkttyp, amount)
                VALUES (?, ?, ?)
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bestellung.getBestellungId());
            statement.setInt(2, produkt.getId());
            statement.setInt(3, menge);
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Produkt konnte nicht zur Bestellung hinzugefügt werden");
            }
        }
    }

    @Override
    public List<Bestellung> findAll() throws SQLException {
        var sql = """
                SELECT bestellnummer, bestelldatum, kunde, mitarbeiter
                FROM bestellungen
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var found = new ArrayList<Bestellung>();
            while (resultSet.next()) {
                bestellungOfResultSet(resultSet).ifPresent(found::add);
            }
            return found;
        }
    }

    @Override
    public Bestellung save(Bestellung entity) throws SQLException {
        var sql = """
                insert into bestellungen (bestelldatum, kunde, mitarbeiter)
                values (?, ?, ?)
                """;
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDate(1, Date.valueOf(entity.getBestellDatum()));
            statement.setInt(2, entity.getKunde().getId());
            if (entity.getMitarbeiter() != null) {
                statement.setString(3, entity.getMitarbeiter().getNamenskuerzel());
            } else {
                statement.setString(3, null);
            }
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Bestellung konnte nicht gespeichert werden!");
            var generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next(); // ansonsten wird vorher die exception geworfen
            return entity.withBestellungId(generatedKeys.getInt(1));

        }
    }

    @Override
    public void delete(Bestellung entity) throws SQLException {
        var sql = """
                delete from bestellungen
                where bestellnummer = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getBestellungId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Bestellung konnte nicht gelöscht werden!");
        }
    }

    @Override
    public void update(Bestellung entity) throws SQLException {
        var sql = """
                update bestellungen
                set bestelldatum = ?, kunde = ?, mitarbeiter = ?
                where bestellnummer = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(entity.getBestellDatum()));
            statement.setInt(2, entity.getKunde().getId());
            if (entity.getMitarbeiter() != null) {
                statement.setString(3, entity.getMitarbeiter().getNamenskuerzel());
            } else {
                statement.setString(3, null);
            }
            statement.setInt(4, entity.getBestellungId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Bestellung konnte nicht geupdated werden!");
        }
    }

    @Override
    public Optional<Bestellung> findById(Integer primaryKey) throws SQLException {
        var sql = """
                SELECT bestellnummer, bestelldatum, kunde, mitarbeiter
                FROM bestellungen
                WHERE bestellnummer = ?
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, primaryKey);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return bestellungOfResultSet(resultSet);
            }
        }
        return Optional.empty();
    }
}
