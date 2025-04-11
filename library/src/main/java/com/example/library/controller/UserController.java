package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final BookService bookService;

    public UserController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> viewBooks() {
        return bookService.getAllBooks();
    }

    @PutMapping("/borrow/{id}")
    public Optional<Book> borrowBook(@PathVariable Integer id) {
        return bookService.borrowBook(id);
    }

    @PutMapping("/return/{id}")
    public Optional<Book> returnBook(@PathVariable Integer id) {
        return bookService.returnBook(id);
    }

    @GetMapping("/search")
    public List<Book> searchBook(@RequestParam String title, @RequestParam String author) {
        return bookService.searchBooks(title, author);
    }
}
