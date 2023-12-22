package org.example.dao;

import org.example.entity.Author;

import java.util.List;

public interface AuthorDAO {
    void addAuthor(Author author);

    List<Author> getAllAuthors();

    Author findAuthorById(int id);

    void deleteAuthor(int id);
}
