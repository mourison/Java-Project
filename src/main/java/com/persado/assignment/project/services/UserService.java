package com.persado.assignment.project.services;

import com.persado.assignment.project.domain.User;
import com.persado.assignment.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public void storeUser(User user) {
        Optional<User> existingUser = findUserById(user.getUserId());
        if (!existingUser.isPresent()) {
            userRepository.save(user);
        }
    }

    public Boolean deleteUser(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            if (hasCurrentlyLoanedBooks(user.get())) {
                return false;
            } else {
                deleteUserByIsbn(user.get().getUserId());
                return true;
            }
        }
        return false;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    private void deleteUserByIsbn(long isbn) {
        userRepository.deleteById(isbn);
    }

    private Boolean hasCurrentlyLoanedBooks(User user) {
        return user.getRegistrations().stream()
                .anyMatch(bookRegistration -> bookRegistration.getReturnedAt() == null);
    }

}
