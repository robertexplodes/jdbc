package persistence;

import domain.Holzart;
import domain.Produkt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JdbcProduktRepository(Connection connection) implements ProduktRepository {

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
    public void save(Produkt entity) throws SQLException {
        var sql = """
                INSERT INTO produkttypen (produkttyp_id, produktart, holzart)
                VALUES (?, ?, ?)
                    """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getProduktart());
            statement.setString(3, entity.getHolzart().name());
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Produkt konnte nicht gespeichert werden");
            }
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
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Produkt konnte nicht gelöscht werden");
            }
        }
    }

    @Override
    public Optional<Produkt> findByPrimaryKey(Integer primaryKey) throws SQLException {
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
