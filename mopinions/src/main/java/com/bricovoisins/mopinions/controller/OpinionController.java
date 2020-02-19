package com.bricovoisins.mopinions.controller;

import com.bricovoisins.mopinions.dao.OpinionDao;
import com.bricovoisins.mopinions.model.Opinion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class OpinionController {
    @Autowired
    OpinionDao opinionDao;

    @PostMapping(value = "/add_opinion")
    public ResponseEntity<Void> insertOpinion(@RequestBody Opinion opinion) {
        Opinion addedOpinion = opinionDao.save(opinion);

        if (addedOpinion == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedOpinion.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/allOpinions")
    public List<Opinion> getAllOpinions() {
        return opinionDao.findAll();
    }

    @GetMapping(value = "/opinions_author/{authorId}")
    public List<Opinion> getAllOpinionsByAuthor(@PathVariable int authorId) {
        return opinionDao.findAllByAuthorId(authorId);
    }

    @GetMapping(value = "/opinions_user/{userId}")
    public List<Opinion> getAllOpinionsByUser(@PathVariable int userId) {
        return opinionDao.findAllByUserIdOrderByIdDesc(userId);
    }

    @GetMapping(value = "/opinion/{id}")
    public Opinion getOpinion(@PathVariable int id) {
        return opinionDao.findById(id);
    }

    @GetMapping(value = "/remove_opinion/{id}")
    public void deleteOpinion(@PathVariable int id) {
        opinionDao.deleteById(id);
    }
}
