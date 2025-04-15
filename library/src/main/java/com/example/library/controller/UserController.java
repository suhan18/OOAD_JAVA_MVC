package com.example.library.controller;
import com.example.library.service.ReturnResult;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import com.example.library.service.ReturnResult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final BookService bookService;

    public UserController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String viewBooks(Model model) {
        System.out.println("Fetching books...");
        List<Book> books = bookService.getAllBooks();
        System.out.println("Books retrieved: " + books.size()); // Log the number of books
        model.addAttribute("books", books);
        return "user";
    }


    @PostMapping("/borrow")
    public String borrowBook(@RequestParam("id") Integer id, Model model) {
        Optional<Book> borrowed = bookService.borrowBook(id);
        if (borrowed.isPresent()) {
            model.addAttribute("successMessage", "Book borrowed successfully!");
        } else {
            model.addAttribute("errorMessage", "Book is not available to borrow.");
        }
        model.addAttribute("books", bookService.getAllBooks());
        return "user";
    }

    @PostMapping("/return")
public String returnBook(@RequestParam("id") Integer id,
                         @RequestParam("returnDate") String returnDate,
                         Model model) {

    Optional<ReturnResult> result = bookService.returnBookWithFine(id, returnDate);

    if (result.isPresent()) {
        model.addAttribute("successMessage", "Book returned successfully!");
        model.addAttribute("fineMessage", "Fine: ₹" + result.get().getFine());
    } else {
        model.addAttribute("errorMessage", "Return failed. Book may not be checked out.");
    }

    model.addAttribute("books", bookService.getAllBooks());
    return "user";
}



    @GetMapping("/search")
    public String searchBook(@RequestParam String title,
                             @RequestParam String author,
                             Model model) {
        List<Book> results = bookService.searchBooks(title, author);
        model.addAttribute("books", results);
        return "user";
    }
}
