package org.example.dao;

import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Country;
import org.example.listener.BookDAOListener;

import java.util.List;
import java.util.Set;

public interface BookDAO {
    void addBook(Book book);

    List<Book> getAllBooks();

    Book getBookById(int bookId);

    Country getCountryByBookId(int countryId);

    Set<Author> getAuthorsByBookId(int bookId);

    void deleteBook(int bookId);

    void updateBook(Book book);

    void addListener(BookDAOListener listener);


    void removeListener(BookDAOListener listener);
}
