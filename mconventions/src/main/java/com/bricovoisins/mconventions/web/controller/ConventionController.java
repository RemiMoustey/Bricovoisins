package com.bricovoisins.mconventions.web.controller;

import com.bricovoisins.mconventions.dao.ConventionDao;
import com.bricovoisins.mconventions.model.Convention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ConventionController {
    @Autowired
    ConventionDao conventionDao;

    @PostMapping(value = "/insert_message")
    public ResponseEntity<Void> insertUser(@RequestBody Convention convention) {
        Convention addedConvention = conventionDao.save(convention);

        if (addedConvention == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedConvention.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
