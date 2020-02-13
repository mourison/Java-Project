package com.persado.assignment.project.controllers;


import com.persado.assignment.project.domain.User;
import com.persado.assignment.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("manage/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("user")
    public ResponseEntity<Optional<User>> findUser(@RequestParam Long id) {
        Optional<User> user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userService.storeUser(user);
        return new ResponseEntity<>("Create new user", HttpStatus.OK);
    }

    @DeleteMapping("user")
    public ResponseEntity<String> deleteUser(@RequestParam Long id) {
        Boolean isUserDeleted = userService.deleteUser(id);
        if (!isUserDeleted) {
            return new ResponseEntity<>("Cannot delete the user", HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

