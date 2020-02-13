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

    @GetMapping(value = "/conventions")
    public List<Convention> getAllConventions() {
        return conventionDao.findAll();
    }

    @GetMapping(value = "/convention/{id}")
    public Convention getOneConventionById(@PathVariable int id) {
        return conventionDao.findById(id);
    }

    @GetMapping(value = "/conventions_sender/{senderId}")
    public List<Convention> getAllConventionsOfSender(@PathVariable int senderId) {
        return conventionDao.findAllBySenderId(senderId);
    }

    @RequestMapping(value = "/delete_convention/{id}")
    public void deleteConvention(@PathVariable int id) {
        conventionDao.deleteById(id);
    }

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

    @GetMapping(value = "/conventions_recipient/{recipientId}")
    public List<Convention> getListNoValidatedConventionsHelper(@PathVariable int recipientId) {
        return conventionDao.findByRecipientIdAndIsValidatedByRecipientIsFalse(recipientId);
    }

    @GetMapping(value = "/validated_conventions/{userId}")
    public List<Convention> getListValidatedConventionsUser(@PathVariable int userId) {
        return conventionDao.findBySenderIdAndIsValidatedByRecipientIsTrueAndIsEndedBySenderIsFalse(userId);
    }

    @GetMapping(value = "/validated_conventions_recipient/{recipientId}")
    public List<Convention> getListValidatedConventionsHelper(@PathVariable int recipientId) {
        return conventionDao.findByRecipientIdAndIsValidatedByRecipientIsTrueAndIsEndedBySenderIsFalse(recipientId);
    }

    @GetMapping(value = "/ended_conventions/{userId}")
    public List<Convention> getListEndedConventionsSender(@PathVariable int userId) {
        return conventionDao.findBySenderIdAndIsEndedBySenderIsTrue(userId);
    }

    @GetMapping(value = "/ended_conventions_recipient/{userId}")
    public List<Convention> getListEndedConventionsRecipient(@PathVariable int userId) {
        return conventionDao.findByRecipientIdAndIsEndedBySenderIsTrue(userId);
    }

    @RequestMapping(value = "/update_convention", method = RequestMethod.PUT)
    public void updateConvention(@RequestBody Convention convention) {
        conventionDao.save(convention);
    }
}
