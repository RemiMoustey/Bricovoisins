package com.bricovoisins.musers.web.controller;

import com.bricovoisins.musers.dao.UserDao;
import com.bricovoisins.musers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
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

    @GetMapping(value = "/utilisateurId/{userId}")
    public User getOneUser(@PathVariable int userId) {
        return userDao.findById(userId);
    }

    @GetMapping(value = "/supprimer_utilisateur/{id}")
    public void deleteUser(@PathVariable int id) {
        userDao.deleteById(id);
    }

    @GetMapping(value = "/utilisateur/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userDao.findByEmail(email);
    }

    @PostMapping(value = "/results")
    public List<User> getSearchedUsers(@RequestParam String search) {
        if (search.equals("gardening")) {
            return userDao.findAllByLevelGardeningIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("electricity")) {
            return userDao.findAllByLevelElectricityIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("plumbing")) {
            return userDao.findAllByLevelPlumbingIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("carpentry")) {
            return userDao.findAllByLevelCarpentryIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("painting")) {
            return userDao.findAllByLevelPaintingIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("masonry")) {
            return userDao.findAllByLevelMasonryIsNotNullOrLevelDiyIsNotNull();
        } else if (search.equals("diy")) {
            return userDao.findAllByLevelDiyIsNotNull();
        } else {
            return Collections.emptyList();
        }
    }
}
