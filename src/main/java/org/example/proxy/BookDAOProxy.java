package org.example.proxy;

import org.example.dao.BookDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Country;
import org.example.listener.BookDAOListener;

import java.util.List;
import java.util.Set;

public class BookDAOProxy implements BookDAO {
    private BookDAO bookDAO;
    private String userRole;

    public BookDAOProxy(BookDAO bookDAO, String userRole) {
        this.bookDAO = bookDAO;
        this.userRole = userRole;
    }

    @Override
    public void addBook(Book book) {
        if ("ADMIN".equals(userRole)) {
            bookDAO.addBook(book);
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    @Override
    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    @Override
    public Country getCountryByBookId(int countryId) {
        return bookDAO.getCountryByBookId(countryId);
    }

    @Override
    public Set<Author> getAuthorsByBookId(int bookId) {
        return bookDAO.getAuthorsByBookId(bookId);
    }

    @Override
    public void deleteBook(int bookId) {
        if ("ADMIN".equals(userRole)) {
            bookDAO.deleteBook(bookId);
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public void updateBook(Book book) {
        if ("ADMIN".equals(userRole)) {
            bookDAO.updateBook(book);
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public void addListener(BookDAOListener listener) {
        bookDAO.addListener(listener);
    }

    @Override
    public void removeListener(BookDAOListener listener) {
        bookDAO.removeListener(listener);
    }
}
