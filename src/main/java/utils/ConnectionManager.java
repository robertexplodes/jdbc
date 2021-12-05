package utils;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ConnectionManager {

    private ConnectionManager() {}

    private static Connection connection;

    @SneakyThrows
    public static Connection getConnection() {
        if (connection != null)
            return connection;
        String file = "/database.conf";
        var url = ConnectionManager.class.getResource(file);
        var path = Path.of(url.toURI());
        try (var lines = Files.lines(path)) {
            var values = lines.map(l -> l.split("="))
                    .collect(Collectors.toMap(
                            s -> s[0],
                            s -> s[1],
                            (v1, v2) -> v1,
                            HashMap::new
                    ));
            connection = DriverManager.getConnection(values.get("url"), values.get("user"), values.get("password"));
        } catch (SQLException e) {
            throw new IllegalArgumentException("Could not connect to Database. Please check your config file.");
        }
        return connection;
    }
}
