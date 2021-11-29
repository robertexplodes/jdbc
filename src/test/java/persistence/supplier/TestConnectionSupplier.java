package persistence.supplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionSupplier {

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("""
                jdbc:h2:mem:test;\
                init=runscript from 'classpath:/schema.sql'
                """);
    }
}