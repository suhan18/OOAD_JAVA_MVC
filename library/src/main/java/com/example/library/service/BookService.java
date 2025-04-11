package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repo;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Book addBook(Book book) {
        book.setStatus("Available");
        book.setDueDate(null);
        return repo.save(book);
    }

    public Optional<Book> updateBook(Integer id, Book updated) {
        return repo.findById(id).map(book -> {
            book.setTitle(updated.getTitle());
            book.setAuthor(updated.getAuthor());
            book.setStatus(updated.getStatus());
            book.setDueDate(updated.getDueDate());
            return repo.save(book);
        });
    }

    public void deleteBook(Integer id) {
        repo.deleteById(id);
    }

    public Optional<Book> borrowBook(Integer bookId) {
        return repo.findById(bookId).map(book -> {
            if ("Available".equalsIgnoreCase(book.getStatus())) {
                book.setStatus("CheckedOut");
                book.setDueDate(LocalDate.now().plusDays(14).format(formatter));
                return repo.save(book);
            }
            return null;
        });
    }

    public Optional<Book> returnBook(Integer bookId) {
        return repo.findById(bookId).map(book -> {
            book.setStatus("Available");
            book.setDueDate(null);
            return repo.save(book);
        });
    }

    public List<Book> searchBooks(String title, String author) {
        return repo.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author);
    }
}
