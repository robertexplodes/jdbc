package persistence;

import domain.Mitarbeiter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JdbcMitarbeiterRepository(Connection connection) implements MitarbeiterRepository {

    private Optional<Mitarbeiter> mitarbeiterOfResultset(ResultSet resultSet) throws SQLException {
        var namenskuerzel = resultSet.getString("namenskuerzel");
        var name = resultSet.getString("name");
        var rolle = resultSet.getString("rolle");
        var monatsgehalt = resultSet.getDouble("monatsgehalt");
        return Optional.of(new Mitarbeiter(namenskuerzel, name, rolle, monatsgehalt));
    }

    @Override
    public List<Mitarbeiter> findByName(String name) throws SQLException {
        var sql = """
                SELECT namenskuerzel, name, rolle, monatsgehalt
                FROM mitarbeiter
                WHERE name LIKE ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            String like = "%" + name + "%";
            statement.setString(1, like);
            var found = new ArrayList<Mitarbeiter>();
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                var mitarbeiter = mitarbeiterOfResultset(resultSet);
                mitarbeiter.ifPresent(found::add);
            }
            return found;
        }
    }

    @Override
    public List<Mitarbeiter> findAll() throws SQLException {
        var sql = """
                SELECT namenskuerzel, name, rolle, monatsgehalt
                FROM mitarbeiter
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var found = new ArrayList<Mitarbeiter>();
            while (resultSet.next()) {
                var mitarbeiter = mitarbeiterOfResultset(resultSet);
                mitarbeiter.ifPresent(found::add);
            }
            return found;
        }
    }

    @Override
    public Mitarbeiter save(Mitarbeiter mitarbeiter) throws SQLException {
        var sql = """
                INSERT INTO mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
                VALUES (?, ?, ?, ?)
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, mitarbeiter.getNamenskuerzel());
            statement.setString(2, mitarbeiter.getName());
            statement.setString(3, mitarbeiter.getRolle().name());
            statement.setDouble(4, mitarbeiter.getGehalt());
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Mitarbeiter konnte nicht gespeichert werden");
            }
            return mitarbeiter;
        }
    }

    @Override
    public void delete(Mitarbeiter entity) throws SQLException {
        var slq = """
                DELETE FROM mitarbeiter
                WHERE namenskuerzel = ?
                """;
        try (var statement = connection.prepareStatement(slq)) {
            statement.setString(1, entity.getNamenskuerzel());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Mitarbeiter konnte nicht gelöscht werden");
        }
    }

    @Override
    public void update(Mitarbeiter entity) throws SQLException {
        var sql = """
                UPDATE mitarbeiter
                SET name = ?, rolle = ?, monatsgehalt = ?
                WHERE namenskuerzel = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getRolle().name());
            statement.setDouble(3, entity.getGehalt());
            statement.setString(4, entity.getNamenskuerzel());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Mitarbeiter konnte nicht aktualisiert werden");
        }
    }

    @Override
    public Optional<Mitarbeiter> findById(String namenskuerzel) throws SQLException {
        var sql = """
                SELECT namenskuerzel, name, rolle, monatsgehalt
                FROM mitarbeiter
                WHERE namenskuerzel = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, namenskuerzel);
            var resultSet = statement.executeQuery();
            if (resultSet.next())
                return mitarbeiterOfResultset(resultSet);
        }
        return Optional.empty();
    }
}
