package org.example.listener;

import org.example.entity.Book;

public interface BookDAOListener {
    void onBookAdded(Book book);
    void onBookUpdated(Book book);
    void onBookDeleted(int bookId);
}
