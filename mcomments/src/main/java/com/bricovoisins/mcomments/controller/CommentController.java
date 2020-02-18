package com.bricovoisins.mcomments.controller;

import com.bricovoisins.mcomments.dao.CommentDao;
import com.bricovoisins.mcomments.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    CommentDao commentDao;

    @PostMapping(value = "/add_comment")
    public ResponseEntity<Void> insertComment(@RequestBody Comment comment) {
        Comment addedComment = commentDao.save(comment);

        if (addedComment == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedComment.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/allComments")
    public List<Comment> getAllComments() {
        return commentDao.findAll();
    }

    @GetMapping(value = "/comments_author/{authorId}")
    public List<Comment> getAllCommentsByAuthor(@PathVariable int authorId) {
        return commentDao.findAllByAuthorId(authorId);
    }

    @GetMapping(value = "/comments_user/{userId}")
    public List<Comment> getAllCommentsByUser(@PathVariable int userId) {
        return commentDao.findAllByUserIdOrderByIdDesc(userId);
    }

    @GetMapping(value = "/comment/{id}")
    public Comment getComment(@PathVariable int id) {
        return commentDao.findById(id);
    }

    @GetMapping(value = "/remove_comment/{id}")
    public void deleteComment(@PathVariable int id) {
        commentDao.deleteById(id);
    }
}
