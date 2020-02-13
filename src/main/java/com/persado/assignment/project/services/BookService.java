package com.persado.assignment.project.services;

import com.persado.assignment.project.domain.Book;
import com.persado.assignment.project.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void storeBook(Book book) {
        Optional<Book> existingBook = findBookByIsbn(book.getIsbn());
        if (!existingBook.isPresent()) {
            bookRepository.save(book);
        }
    }

    public Optional<Book> findBookByIsbn(Long isbn) {
        return bookRepository.findById(isbn);
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    public Boolean deleteBook(Long isbn) {
        Optional<Book> book = findBookByIsbn(isbn);
        if (book.isPresent()) {
            if (hasCurrentlyLoanedRegistrations(book.get())) {
                return false;
            } else {
                deleteBookById(book.get().getIsbn());
                return true;
            }
        }
        return false;
    }


    private void deleteBookById(long id) {
        bookRepository.deleteById(id);
    }

    private Boolean hasCurrentlyLoanedRegistrations(Book book) {
        return book.getRegistrations().stream()
                .anyMatch(bookRegistration -> bookRegistration.getReturnedAt() == null);
    }

}
