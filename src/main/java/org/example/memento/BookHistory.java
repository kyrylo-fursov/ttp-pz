package org.example.memento;

import org.example.entity.Book;

import java.util.Deque;
import java.util.LinkedList;

public class BookHistory {
    private final Deque<BookMemento> history = new LinkedList<>();

    public void save(Book book) {
        history.push(new BookMemento(book));
    }

    public void undo(Book book) {
        if (!history.isEmpty()) {
            BookMemento memento = history.pop();
            book.setTitle(memento.getTitle());
            book.setPrice(memento.getPrice());
            book.setAuthors(memento.getAuthors());
            book.setCountry(memento.getCountry());
        }
    }
}
