package org.example.dao.factory;

import org.example.connection.DatabaseConnection;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.dao.CountryDAO;
import org.example.dao.TypeDAO;
import org.example.dao.h2.AuthorDAOh2;
import org.example.dao.h2.BookDAOh2;
import org.example.dao.h2.CountryDAOh2;

public class FabricMethodDAO {
        public static BookDAO getBookDAO(TypeDAO typeDao) {
            return new BookDAOh2(DatabaseConnection.getConnection(typeDao));
        }

        public static AuthorDAO getAuthorDAO(TypeDAO typeDao) {
            return new AuthorDAOh2(DatabaseConnection.getConnection(typeDao));
        }

    public static CountryDAO getCountryDAO(TypeDAO typeDao) {
        return new CountryDAOh2(DatabaseConnection.getConnection(typeDao));
    }
}
