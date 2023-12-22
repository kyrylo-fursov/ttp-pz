package org.example;

import org.example.dao.factory.FabricMethodDAO;
import org.example.dao.factory.TypeDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/testdb", "sa", "")) {
            createTables(connection);
            BookManagementApplication app = new BookManagementApplication(
                    FabricMethodDAO.getBookDAO(TypeDAO.H2),
                    FabricMethodDAO.getCountryDAO(TypeDAO.H2),
                    FabricMethodDAO.getAuthorDAO(TypeDAO.H2));


            app.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP ALL OBJECTS");

            statement.execute("CREATE TABLE IF NOT EXISTS authors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL);");

            statement.execute("CREATE TABLE IF NOT EXISTS countries (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL);");

            statement.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "price DECIMAL(10, 2)," +
                    "country_id INT," +
                    "FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE);");

            statement.execute("CREATE TABLE IF NOT EXISTS book_authors (" +
                    "book_id INT," +
                    "author_id INT," +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE);");

            statement.execute("CREATE TABLE IF NOT EXISTS roles (" +
                    "id INT PRIMARY KEY," +
                    "role_name VARCHAR(50));");

            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY," +
                    "username VARCHAR(50)," +
                    "password VARCHAR(50)," +
                    "role_id INT," +
                    "FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE);");
        }
    }
}
