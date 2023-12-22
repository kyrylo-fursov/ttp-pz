package org.example.connection;

import org.example.dao.TypeDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection h2Connection = null;
    private static Connection mySqlConnection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection(TypeDAO typeDao) {
        switch (typeDao) {
            case H2:
                return getH2Connection();
            case MYSQL:
                return getMySqlConnection();
            default:
                throw new IllegalArgumentException("Unsupported database type");
        }
    }

    private static Connection getH2Connection() {
        if (h2Connection == null) {
            try {
                h2Connection = DriverManager.getConnection("jdbc:h2:~/testdb", "sa", "");
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to H2 database", e);
            }
        }
        return h2Connection;
    }

    private static Connection getMySqlConnection() {
        throw new UnsupportedOperationException("MYSQL is not supported yet");
    }
}
