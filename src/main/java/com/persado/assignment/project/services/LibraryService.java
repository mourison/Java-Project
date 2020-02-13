package com.persado.assignment.project.services;

import com.persado.assignment.project.domain.Book;
import com.persado.assignment.project.domain.BookRegistration;
import com.persado.assignment.project.domain.User;
import com.persado.assignment.project.repositories.BookRegistrationRepository;
import com.persado.assignment.project.repositories.BookRepository;
import com.persado.assignment.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryService {

    private static final int MAX_NUMBER_OF_LOANED_BOOKS = 3;
    private BookRepository bookRepository;

    private UserRepository userRepository;

    private BookRegistrationRepository bookRegistrationRepository;

    @Autowired
    public LibraryService(BookRepository bookRepository, UserRepository userRepository, BookRegistrationRepository bookRegistrationRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookRegistrationRepository = bookRegistrationRepository;
    }

    public Boolean loanBook(Long userId, Long bookId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            boolean canUserLoanOtherBook = checkUserHasMoreThanExpectedNumberOfBooks(user.get());
            Optional<Book> book = bookRepository.findById(bookId);
            if (canUserLoanOtherBook) {
                if (book.isPresent()) {
                    Boolean hasLoadBook = hasLoanedSpecificBook(bookId, user.get());
                    if (!hasLoadBook) {
                        boolean hasAvailableCopies = book.get().getAvailable() > 0 ? true : false;
                        if (hasAvailableCopies) {
                            BookRegistration bookRegistration = createRegistration(book, user);
                            updateBookDetails(book, bookRegistration);
                            updateUserBookRegistrations(user, bookRegistration);
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public Boolean returnBook(Long userId, Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<User> user = userRepository.findById(userId);
        if (book.isPresent() && user.isPresent()) {
            Boolean hasPurchased = book.get().getPurchased() > 0 ? true : false;
            if (hasPurchased) {
                Optional<BookRegistration> bookRegistration = findRegistrationOfBook(book, user);
                if (bookRegistration.isPresent()) {
                    updateBookRegistrationReturnedAt(bookRegistration);
                    updateBookDetails(book, bookRegistration);
                    updateUserDetails(user, bookRegistration);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private Optional<BookRegistration> findRegistrationOfBook(Optional<Book> book, Optional<User> user) {
        return book.get().getRegistrations().stream()
                .filter(user.get().getRegistrations()::contains)
                .filter(bookRegistration1 -> bookRegistration1.getReturnedAt() == null)
                .findFirst();
    }

    private void updateUserDetails(Optional<User> user, Optional<BookRegistration> bookRegistration) {
        updateUserBookRegistrations(user, bookRegistration.get());
        userRepository.save(user.get());
    }

    private void updateBookDetails(Optional<Book> book, Optional<BookRegistration> bookRegistration) {
        book.get().setAvailable(book.get().getAvailable() + 1);
        book.get().setPurchased(book.get().getPurchased() - 1);
        book.ifPresent(book1 -> updateRegistrations(bookRegistration.get(), book1));
        bookRepository.save(book.get());
    }

    private void updateBookRegistrationReturnedAt(Optional<BookRegistration> bookRegistration) {
        bookRegistration.ifPresent(bookRegistration1 -> {
            bookRegistration1.setReturnedAt((LocalDateTime.now()));
            bookRegistrationRepository.save(bookRegistration1);
        });
    }

    private boolean hasLoanedSpecificBook(Long bookId, User user) {
        return user.getRegistrations().stream()
                .anyMatch(bookRegistration -> bookRegistration.getBookId().equals(bookId) && bookRegistration.getReturnedAt() == null);
    }

    private void updateBookDetails(Optional<Book> book, BookRegistration bookRegistration) {
        book.get().setAvailable(book.get().getAvailable() - 1);
        book.get().setPurchased(book.get().getPurchased() + 1);
        book.ifPresent(book1 -> updateRegistrations(bookRegistration, book1));
        bookRepository.save(book.get());
    }

    private BookRegistration createRegistration(Optional<Book> book, Optional<User> user) {
        BookRegistration bookRegistration = new BookRegistration();
        bookRegistration.setLoanedAt(LocalDateTime.now());
        bookRegistration.setUserId(user.get().getUserId());
        bookRegistration.setBookId(book.get().getIsbn());
        return bookRegistrationRepository.save(bookRegistration);
    }

    private void updateUserBookRegistrations(Optional<User> user, BookRegistration bookRegistration) {
        user.ifPresent(user1 -> {
            Set<BookRegistration> registrations = user1.getRegistrations();
            registrations.add(bookRegistration);
            user1.setRegistrations(registrations);
            userRepository.save(user.get());
        });
    }

    private void updateRegistrations(BookRegistration bookRegistration, Book book1) {
        Set<BookRegistration> registrations = book1.getRegistrations();
        registrations.add(bookRegistration);
        book1.setRegistrations(registrations);
    }

    private boolean checkUserHasMoreThanExpectedNumberOfBooks(User user) {
        Long numberOfLoanedBooks = user.getRegistrations().stream()
                .filter(bookRegistration -> bookRegistration.getReturnedAt() == null)
                .count();
        return numberOfLoanedBooks >= MAX_NUMBER_OF_LOANED_BOOKS ? false : true;
    }


}
