package org.example.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Book {
    private int id;
    private String title;
    private Set<Author> authors;
    private BigDecimal price;
    private Country country;

    public Book(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.authors = builder.authors;
        this.price = builder.price;
        this.country = builder.country;
    }

    public Book(Book book) {
        this.id = book.id;
        this.title = book.title;
        this.authors = book.authors;
        this.price = book.price;
        this.country = book.country;
    }

    @Override
    public String toString() {
        return "Book ID: " + id +
                ", Title: " + title +
                ", Price: $" + price +
                ", Country: " + country.getName() +
                ", Authors: " + authors + "\n";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Country getCountry() {
        return country;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public static class Builder {
        private int id;
        private String title;
        private Set<Author> authors = new HashSet<>();
        private BigDecimal price;
        private Country country;

        public Builder(String title) {
            this.title = title;
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthor(Author author) {
            this.authors.add(author);
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withCountry(Country country) {
            this.country = country;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}