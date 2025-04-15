package com.example.library.service;

import com.example.library.model.Book;

public class ReturnResult {
    private Book book;
    private int fine;

    public ReturnResult(Book book, int fine) {
        this.book = book;
        this.fine = fine;
    }

    public Book getBook() {
        return book;
    }

    public int getFine() {
        return fine;
    }
}
