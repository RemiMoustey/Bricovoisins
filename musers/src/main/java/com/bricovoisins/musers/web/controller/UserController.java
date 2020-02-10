package com.bricovoisins.musers.web.controller;

import com.bricovoisins.musers.dao.UserDao;
import com.bricovoisins.musers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @PostMapping(value = "/validation")
    public ResponseEntity<Void> insertUser(@RequestBody User user) {
        User addedUser = userDao.save(user);

        if (addedUser == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/utilisateurs")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping(value = "/supprimer_utilisateur/{id}")
    public void deleteUser(@PathVariable int id) {
        userDao.deleteById(id);
    }

    @GetMapping(value = "/utilisateur/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userDao.findByEmail(email);
    }
}
