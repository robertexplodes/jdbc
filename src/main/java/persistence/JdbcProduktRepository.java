package persistence;

import domain.Holzart;
import domain.Produkt;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcProduktRepository implements ProduktRepository {

    private static JdbcProduktRepository instance;
    private final Connection connection;

    private JdbcProduktRepository(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public static JdbcProduktRepository getInstance(Connection connection) {
        if (instance == null || instance.connection.isClosed()) {
            instance = new JdbcProduktRepository(connection);
        }
        return instance;
    }


    private Optional<Produkt> produktOfResultSet(ResultSet resultSet) throws SQLException {
        var produktart = resultSet.getString("produktart");
        var holzart = Holzart.valueOf(resultSet.getString("holzart"));
        var id = resultSet.getInt("produkttyp_id");
        try {
            return Optional.of(new Produkt(id, holzart, produktart));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Produkt> findAllByHolzart(Holzart holzart) throws SQLException {
        var sql = """
                SELECT produkttyp_id, produktart, holzart
                FROM produkttypen
                WHERE holzart = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, holzart.name());
            var resultSet = statement.executeQuery();
            var produkte = new ArrayList<Produkt>();
            while (resultSet.next()) {
                produktOfResultSet(resultSet).ifPresent(produkte::add);
            }
            return produkte;
        }
    }

    @Override
    public List<Produkt> findAll() throws SQLException {
        var sql = """
                SELECT produkttyp_id, produktart, holzart
                FROM produkttypen
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var produkte = new ArrayList<Produkt>();
            while (resultSet.next()) {
                produktOfResultSet(resultSet).ifPresent(produkte::add);
            }
            return produkte;
        }
    }

    @Override
    public Produkt save(Produkt entity) throws SQLException {
        var sql = """
                INSERT INTO produkttypen (produktart, holzart)
                VALUES (?, ?)
                    """;
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getProduktart());
            statement.setString(2, entity.getHolzart().name());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Produkt konnte nicht gespeichert werden");
            var keys = statement.getGeneratedKeys();
            keys.next();
            return entity.withId(keys.getInt(1));
        }
    }

    @Override
    public void delete(Produkt entity) throws SQLException {
        var sql = """
                DELETE FROM produkttypen
                WHERE produkttyp_id = ?
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Produkt konnte nicht gelöscht werden");
        }
    }

    @Override
    public void update(Produkt entity) throws SQLException {
        var sql = """
                UPDATE produkttypen
                SET produktart = ?, holzart = ?
                WHERE produkttyp_id = ?
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getProduktart());
            statement.setString(2, entity.getHolzart().name());
            statement.setInt(3, entity.getId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Produkt konnte nicht aktualisiert werden");
        }
    }

    @Override
    public Optional<Produkt> findById(Integer primaryKey) throws SQLException {
        var sql = """
                SELECT produkttyp_id, produktart, holzart
                FROM produkttypen
                WHERE produkttyp_id = ?
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, primaryKey);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return produktOfResultSet(resultSet);
            }
        }
        return Optional.empty();
    }
}
