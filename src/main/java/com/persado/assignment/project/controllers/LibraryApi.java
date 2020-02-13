package com.persado.assignment.project.controllers;

import com.persado.assignment.project.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("library")
public class LibraryApi {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("loan")
    public ResponseEntity<Boolean> rentBook(@RequestParam long userId, @RequestParam long bookIsbn) {
        return new ResponseEntity<>(libraryService.loanBook(userId, bookIsbn), HttpStatus.OK);
    }

    @GetMapping("return")
    public ResponseEntity<Boolean> returnBook(@RequestParam long userId, @RequestParam long bookIsbn) {
        return new ResponseEntity<>(libraryService.returnBook(userId, bookIsbn), HttpStatus.OK);
    }

}
