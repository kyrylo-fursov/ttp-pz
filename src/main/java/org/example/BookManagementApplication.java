package org.example;

import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.dao.CountryDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Country;
import org.example.listener.BookDAOListener;
import org.example.proxy.BookDAOProxy;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BookManagementApplication implements BookDAOListener {
    private static BookDAO bookDAO;
    private static CountryDAO countryDAO;
    private static AuthorDAO authorDAO;
    private Scanner scanner;
    private List<Book> currentBookList;
    private Deque<Book> bookHistory = new LinkedList<>();
    private String currentUserRole;

    public BookManagementApplication(BookDAO bookDAO, CountryDAO countryDAO, AuthorDAO authorDAO) {
        this.bookDAO = bookDAO;
        this.countryDAO = countryDAO;
        this.authorDAO = authorDAO;
        this.scanner = new Scanner(System.in);
        this.currentBookList = bookDAO.getAllBooks();
        displayBooks();
    }

    public void start() {
        authenticateUser();
        bookDAO.addListener(this);
        initializeData();

        int option;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Refresh Book List");
            System.out.println("5. Find author by book id");
            System.out.println("6. Undo last update");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();
            handleUserOption(option);
        } while (option != 0);
    }

    private void authenticateUser() {
        System.out.println("Please log in:");
        System.out.print("Username: ");
        String username = scanner.next();

        System.out.print("Password: ");
        String password = scanner.next();

        // Simplified authentication and role setting
        if ("admin".equals(username) && "adminpass".equals(password)) {
            currentUserRole = "ADMIN";
        } else {
            currentUserRole = "USER";
        }
    }

    private void handleUserOption(int option) {
        switch (option) {
            case 1:
                addBook();
                break;
            case 2:
                updateBook();
                break;
            case 3:
                deleteBook();
                break;
            case 4:
                refreshBookList();
                break;
            case 5:
                getAuthorByBookId();
                break;
            case 6:
                undoLastUpdate();
                break;
            default:
                System.out.println("Invalid option.");

        }
    }

    private void addBook() {
        Book newBook = createBookFromUserInput();

        // Using proxy
        BookDAOProxy proxy = new BookDAOProxy(bookDAO, currentUserRole);
        try {
            proxy.addBook(newBook);
        } catch (RuntimeException e) {
            System.out.println("Error:" + e.getMessage());
            refreshBookList();
        }
    }

    private Book createBookFromUserInput() {
        scanner.nextLine();
        System.out.println("Enter book details...");

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book price: ");
        BigDecimal price = scanner.nextBigDecimal();

        System.out.print("Enter country ID for the book: ");
        int countryId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter author IDs (separated by whitespace): ");
        String authorIdsLine = scanner.nextLine();
        String[] authorIds = authorIdsLine.split("\\s+");

        Book.Builder bookBuilder = new Book.Builder(title).withPrice(price)
                .withCountry(new Country(countryId, ""));

        for (String id : authorIds) {
            int authorId = Integer.parseInt(id);
            bookBuilder.withAuthor(new Author(authorId, ""));
        }

        return bookBuilder.build();
    }

    private void updateBook() {
        System.out.println("Updating a book...");
        System.out.print("Enter the ID of the book to update: ");
        int bookId = scanner.nextInt();

        Book originalBook = bookDAO.getBookById(bookId);
        if (originalBook != null) {
            bookHistory.push(new Book(originalBook));
            Book updatedBook = createBookFromUserInput();
            updatedBook.setId(bookId);

            // Using proxy
            BookDAOProxy proxy = new BookDAOProxy(bookDAO, currentUserRole);
            try {
                proxy.updateBook(updatedBook);
                System.out.println("Book updated successfully.");
            } catch (RuntimeException e) {
                System.out.println("Access denied: " + e.getMessage());
            }
        } else {
            System.out.println("Book not found.");
        }
    }

    private void deleteBook() {
        System.out.println("Deleting a book...");
        System.out.print("Enter the ID of the book to delete: ");
        int bookId = scanner.nextInt();

        // Using proxy
        BookDAOProxy proxy = new BookDAOProxy(bookDAO, currentUserRole);
        try {
            proxy.deleteBook(bookId);
            System.out.println("Book deleted successfully.");
        } catch (RuntimeException e) {
            System.out.println("Access denied: " + e.getMessage());
        }
    }

    private void getAuthorByBookId() {
        System.out.print("Enter the ID of the book: ");
        int bookId = scanner.nextInt();

        Set<Author> authors = bookDAO.getAuthorsByBookId(bookId);
        if (authors != null) {
            System.out.println("Authors:");
            authors.stream().map(Author::getName).forEach(System.out::println);
        } else {
            System.out.println("No author found for this book.");
        }
        refreshBookList();
    }

    private void undoLastUpdate() {
        if (!bookHistory.isEmpty()) {
            Book lastState = bookHistory.pop();

            // Using proxy
            BookDAOProxy proxy = new BookDAOProxy(bookDAO, currentUserRole);
            try {
                proxy.updateBook(lastState);
                System.out.println("Last update undone.");
            } catch (RuntimeException e) {
                System.out.println("Access denied: " + e.getMessage());
            }
        } else {
            System.out.println("No updates to undo.");
        }
    }

    private void refreshBookList() {
        currentBookList = bookDAO.getAllBooks();
        displayBooks();
    }

    private void displayBooks() {
        System.out.println("\nBooks List:");
        currentBookList.forEach(book -> System.out.println(book));
    }

    private static void initializeData() {
        // Insert countries
        Country[] countries = new Country[] {
                new Country(1, "Country 1"),
                new Country(2, "Country 2"),
                // Add more countries...
                new Country(7, "Country 7")
        };
        for (Country country : countries) {
            countryDAO.addCountry(country);
        }

        // Insert authors
        Author[] authors = new Author[] {
                new Author(1, "Author 1"),
                new Author(2, "Author 2"),
                new Author(3, "Author 3")
        };
        for (Author author : authors) {
            authorDAO.addAuthor(author);
        }

        // Insert books
        Book bookOne = new Book.Builder("Book 1").withPrice(BigDecimal.valueOf(100))
                .withCountry(new Country(1, "Country 1"))
                .withAuthor(new Author(1, "Author 1")).build();
        Book bookTwo = new Book.Builder("Book 2").withPrice(BigDecimal.valueOf(150))
                .withCountry(new Country(2, "Country 2"))
                .withAuthor(new Author(2, "Author 2"))
                .withAuthor(new Author(3, "Author 3"))
                .build();
        Book bookThree = new Book.Builder("Book 3").withPrice(BigDecimal.valueOf(200))
                .withCountry(new Country(3, "Country 3"))
                .withAuthor(new Author(3, "Author 3")).build();

        bookDAO.addBook(bookOne);
        bookDAO.addBook(bookTwo);
        bookDAO.addBook(bookThree);
    }

    @Override
    public void onBookAdded(Book book) {
        System.out.println("Book added");
        refreshBookList();
    }

    @Override
    public void onBookUpdated(Book book) {
        refreshBookList();
    }

    @Override
    public void onBookDeleted(int bookId) {
        System.out.println("Book deleted");
        refreshBookList();
    }
}
