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

    public Optional<ReturnResult> returnBookWithFine(Integer bookId, String returnDateStr) {
        return repo.findById(bookId).map(book -> {
            if ("CheckedOut".equalsIgnoreCase(book.getStatus())) {
                int fine = calculateFine(book, returnDateStr);
                book.setStatus("Available");
                book.setDueDate(null); // clear due date after calculating
                Book updatedBook = repo.save(book);
                return new ReturnResult(updatedBook, fine);
            }
            return null;
        });
    }
    
    
    public int calculateFine(Book book, String returnDateStr) {
        if (book.getDueDate() == null) return 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(book.getDueDate(), formatter);
        LocalDate returnDate = LocalDate.parse(returnDateStr, formatter);
    
        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
        return (int) (daysLate > 0 ? daysLate * 10 : 0); // ₹10 per day
    }
    

    public List<Book> searchBooks(String title, String author) {
        return repo.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author);
    }
}
