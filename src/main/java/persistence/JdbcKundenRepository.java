package persistence;

import domain.Bestellung;
import domain.Kunde;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcKundenRepository implements KundenRepository {

    private static JdbcKundenRepository instance = null;
    private final Connection connection;

    private JdbcKundenRepository(Connection connection) {
        this.connection = connection;
    }
    @SneakyThrows
    public static JdbcKundenRepository getInstance(Connection connection) {
        if (instance == null || instance.connection.isClosed())
            instance = new JdbcKundenRepository(connection);
        return instance;
    }

    private Optional<Kunde> kundeOfResultSet(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("kunden_id");
        var email = resultSet.getString("email");
        var name = resultSet.getString("name");
        return Optional.of(new Kunde(id, name, email));
    }

    @Override
    public List<Bestellung> findAllBestellungen(Kunde kunde) throws SQLException {
        var bestellungRepository = JdbcBestellungRepository.getInstance(connection);
        return bestellungRepository.findAllByKunde(kunde);
    }

    @Override
    public List<Kunde> findAll() throws SQLException {
        var sql = """
                select kunden_id, email, name
                from kunden
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var kunden = new ArrayList<Kunde>();
            while (resultSet.next()) {
                kundeOfResultSet(resultSet).ifPresent(kunden::add);
            }
            return kunden;
        }
    }

    @Override
    public Kunde save(Kunde entity) throws SQLException {
        var sql = """
                insert into kunden(email, name)
                values (?, ?)
                """;
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getName());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Kunde konnte nicht gespeichert werden!");
            var keys = statement.getGeneratedKeys();
            keys.next();
            return entity.withId(keys.getInt(1));
        }
    }

    @Override
    public void delete(Kunde entity) throws SQLException {
        var sql = """
                delete from kunden
                where kunden_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Kunde konnte nicht gelöscht werden!");
        }
    }

    @Override
    public void update(Kunde entity) throws SQLException {
        var sql = """
                update kunden
                set email = ?, name = ?
                where kunden_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getId());
            int i = statement.executeUpdate();
            if (i == 0)
                throw new SQLException("Kunde konnte nicht aktualisiert werden!");
        }
    }

    @Override
    public Optional<Kunde> findById(Integer primaryKey) throws SQLException {
        var sql = """
                select kunden_id, email, name
                from kunden
                where kunden_id = ?
                                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, primaryKey);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return kundeOfResultSet(resultSet);
            }
        }
        return Optional.empty();
    }
}
