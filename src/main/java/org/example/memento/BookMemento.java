package org.example.memento;

import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Country;

import java.math.BigDecimal;
import java.util.Set;

public class BookMemento {
    private final String title;
    private final BigDecimal price;
    private final Set<Author> authors;
    private final Country country;

    public BookMemento(Book book) {
        this.title = book.getTitle();
        this.price = book.getPrice();
        this.authors = book.getAuthors();
        this.country = book.getCountry();
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Country getCountry() {
        return country;
    }
}
