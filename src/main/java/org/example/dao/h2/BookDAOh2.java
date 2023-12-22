package org.example.dao.h2;

import org.example.dao.BookDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Country;
import org.example.listener.BookDAOListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDAOh2 implements BookDAO {
    private Connection connection;

    public BookDAOh2(Connection connection) {
        this.connection = connection;
    }

    private List<BookDAOListener> listeners = new ArrayList<>();

    @Override
    public void addListener(BookDAOListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(BookDAOListener listener) {
        listeners.remove(listener);
    }

    protected void notifyBookAdded(Book book) {
        for (BookDAOListener listener : listeners) {
            listener.onBookAdded(book);
        }
    }

    protected void notifyBookUpdated(Book book) {
        for (BookDAOListener listener : listeners) {
            listener.onBookUpdated(book);
        }
    }

    protected void notifyBookDeleted(int bookId){
        for (BookDAOListener listener : listeners) {
            listener.onBookDeleted(bookId);
        }
    }

    @Override
    public void addBook(Book book) {
        String sqlBook = "INSERT INTO books (title, price, country_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlBook, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setInt(3, book.getCountry().getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }

            String sqlBookAuthors = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";
            for (Author author : book.getAuthors()) {
                try (PreparedStatement stmt = connection.prepareStatement(sqlBookAuthors)) {
                    stmt.setInt(1, book.getId());
                    stmt.setInt(2, author.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        notifyBookAdded(book);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book.Builder(resultSet.getString("title"))
                        .withPrice(resultSet.getBigDecimal("price"))
                        .build();
                book.setId(resultSet.getInt("id"));

                // Fetch and set country
                Country country = getCountryByBookId(resultSet.getInt("country_id"));
                book.setCountry(country);

                // Fetch and set authors
                Set<Author> authors = getAuthorsByBookId(book.getId());
                book.setAuthors(authors);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book.Builder(resultSet.getString("title"))
                            .withPrice(resultSet.getBigDecimal("price"))
                            .build();
                    book.setId(resultSet.getInt("id"));

                    // Fetch and set country
                    Country country = getCountryByBookId(resultSet.getInt("country_id"));
                    book.setCountry(country);

                    // Fetch and set authors
                    Set<Author> authors = getAuthorsByBookId(book.getId());
                    book.setAuthors(authors);

                    return book;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Country getCountryByBookId(int countryId) {
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, countryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Country(resultSet.getInt("id"), resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Author> getAuthorsByBookId(int bookId) {
        Set<Author> authors = new HashSet<>();
        String sql = "SELECT a.id, a.name FROM authors a INNER JOIN book_authors ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    authors.add(new Author(resultSet.getInt("id"), resultSet.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting book failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        notifyBookDeleted(bookId);
    }

    @Override
    public void updateBook(Book book) {
        String sqlUpdateBook = "UPDATE books SET title = ?, price = ?, country_id = ? WHERE id = ?";
        String sqlDeleteAuthors = "DELETE FROM book_authors WHERE book_id = ?";
        String sqlInsertAuthors = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";

        try (PreparedStatement statementUpdateBook = connection.prepareStatement(sqlUpdateBook);
             PreparedStatement statementDeleteAuthors = connection.prepareStatement(sqlDeleteAuthors);
             PreparedStatement statementInsertAuthors = connection.prepareStatement(sqlInsertAuthors)) {

            statementUpdateBook.setString(1, book.getTitle());
            statementUpdateBook.setBigDecimal(2, book.getPrice());
            statementUpdateBook.setInt(3, book.getCountry().getId());
            statementUpdateBook.setInt(4, book.getId());
            statementUpdateBook.executeUpdate();

            statementDeleteAuthors.setInt(1, book.getId());
            statementDeleteAuthors.executeUpdate();

            for (Author author : book.getAuthors()) {
                statementInsertAuthors.setInt(1, book.getId());
                statementInsertAuthors.setInt(2, author.getId());
                statementInsertAuthors.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        notifyBookUpdated(book);
    }
}