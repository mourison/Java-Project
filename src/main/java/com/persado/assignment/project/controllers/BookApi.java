package com.persado.assignment.project.controllers;


import com.persado.assignment.project.domain.Book;
import com.persado.assignment.project.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("manage/books")
public class BookApi {

    @Autowired
    private BookService bookService;


    @PostMapping("book")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        bookService.storeBook(book);
        return new ResponseEntity<>("Create new book", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping("book")
    public ResponseEntity<String> deleteUser(@RequestParam Long isbn) {
        Boolean isBookDeleted = bookService.deleteBook(isbn);
        if (!isBookDeleted) {
            return new ResponseEntity<>("Cannot delete the book", HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();
        }
    }



}
