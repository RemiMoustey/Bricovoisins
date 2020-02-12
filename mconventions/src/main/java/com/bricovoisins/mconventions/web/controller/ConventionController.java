package com.bricovoisins.mconventions.web.controller;

import com.bricovoisins.mconventions.dao.ConventionDao;
import com.bricovoisins.mconventions.model.Convention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ConventionController {
    @Autowired
    ConventionDao conventionDao;

    @PostMapping(value = "/send_convention")
    public ResponseEntity<Void> insertConvention(@RequestBody Convention convention) {
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

    @GetMapping(value = "/conventions/{userId}")
    public List<Convention> getListCurrentConventionsUser(@PathVariable int userId) {
        return conventionDao.findBySenderIdAndIsValidatedByRecipientIsFalse(userId);
    }

    @GetMapping(value = "/validated_conventions/{userId}")
    public List<Convention> getListValidatedConventionsUser(@PathVariable int userId) {
        return conventionDao.findBySenderIdAndIsValidatedByRecipientIsTrue(userId);
    }

    @GetMapping(value = "/ended_conventions/{userId}")
    public List<Convention> getListEndedConventionsUser(@PathVariable int userId) {
        return conventionDao.findBySenderIdAndIsEndedBySenderIsTrue(userId);
    }
}
