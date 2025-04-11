package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import com.example.library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final MemberService memberService;
    private final BookRepository bookRepository;

    public AdminController(BookService bookService, MemberService memberService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String showAdminPage(Model model, @ModelAttribute("successMessage") String successMessage) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("book", new Book());
        model.addAttribute("bookToUpdate", new Book());

        model.addAttribute("successMessage", successMessage);
        return "admin";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        bookService.addBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/admin";
    }

    // Find book for update
    @GetMapping("/findBookByIsbn")
    public String findBookByIsbn(@RequestParam("isbn") String isbn, Model model) {
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("bookToUpdate", book);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("book", new Book());
        return "admin";
        // name of your .html file
    }

    @GetMapping("/updateBook")
    public String showUpdateForm(@RequestParam("isbn") String isbn, Model model) {
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("bookToUpdate", book);
        return "admin"; // like "editBook.html"
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("bookToUpdate") Book updatedBook, RedirectAttributes redirectAttributes) {
        Book existingBook = bookRepository.findByIsbn(updatedBook.getIsbn());

        if (existingBook != null) {

            // Update fields
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setStatus(updatedBook.getStatus());
            existingBook.setPublisher(updatedBook.getPublisher());
            existingBook.setEdition(updatedBook.getEdition());
            existingBook.setAvailable(updatedBook.isAvailable());
            existingBook.setDueDate(updatedBook.getDueDate());

            // Save updated book
            bookRepository.save(existingBook);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Book with ISBN " + updatedBook.getIsbn() + " not found.");
        }

        return "redirect:/admin";
    }

    @GetMapping("/deleteBook")
    public String showDeleteConfirmation(@RequestParam("isbn") String isbn, Model model) {
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("bookToDelete", book);
        return "admin"; // or a dedicated "confirm-delete" template if you prefer
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book != null) {
            bookRepository.delete(book);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Book with ISBN " + isbn + " not found.");
        }
        return "redirect:/admin";
    }

}
