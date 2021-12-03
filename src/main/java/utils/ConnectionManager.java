package utils;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectionManager {

    private ConnectionManager() {}

    private static Connection connection;
    private static Map<String, String> values;

    @SneakyThrows
    public static Connection getConnection() {
        if (connection != null)
            return connection;

        initValues("/database.conf");
        try {
            connection = DriverManager.getConnection(values.get("url"), values.get("username"), values.get("password"));
        } catch (SQLException e) {
            throw new IllegalArgumentException("Config file is not valid");
        }
        return connection;
    }

    @SneakyThrows
    private static void initValues(@NonNull String file) throws IOException {
        var url = ConnectionManager.class.getResource(file);
        var path = Path.of(url.toURI());
        try (var lines = Files.lines(path)) {
            values = lines.map(l -> l.split("="))
                    .collect(Collectors.toMap(
                            s -> s[0],
                            s -> s[1],
                            (v1, v2) -> v1,
                            HashMap::new
                    ));
        }
    }
}
