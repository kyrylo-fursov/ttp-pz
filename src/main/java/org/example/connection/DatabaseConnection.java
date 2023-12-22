package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:h2:~/testdb", "sa", "");
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to the database", e);
            }
        }
        return connection;
    }
}
