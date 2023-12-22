package org.example.dao.factory;

import org.example.connection.DatabaseConnection;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.dao.CountryDAO;
import org.example.dao.h2.AuthorDAOh2;
import org.example.dao.h2.BookDAOh2;
import org.example.dao.h2.CountryDAOh2;

public class FabricMethodDAO {
        public static BookDAO getBookDAO(TypeDAO typeDao) {
            switch (typeDao) {
                case H2:
                    return new BookDAOh2(DatabaseConnection.getConnection());
                case MYSQL:
                    throw new UnsupportedOperationException("MYSQL is not supported yet");
                default:
                    throw new IllegalArgumentException("Unknown BookDAO type");
            }
        }

        public static AuthorDAO getAuthorDAO(TypeDAO typeDao) {
            switch (typeDao) {
                case H2:
                    return new AuthorDAOh2(DatabaseConnection.getConnection());
                case MYSQL:
                    throw new UnsupportedOperationException("MYSQL is not supported yet");
                default:
                    throw new IllegalArgumentException("Unknown AuthorDAO type");
            }
        }

        public static CountryDAO getCountryDAO(TypeDAO typeDao) {
            switch (typeDao) {
                case H2:
                    return new CountryDAOh2(DatabaseConnection.getConnection());
                case MYSQL:
                    throw new UnsupportedOperationException("MYSQL is not supported yet");
                default:
                    throw new IllegalArgumentException("Unknown CountryDAO type");
            }
        }
}
