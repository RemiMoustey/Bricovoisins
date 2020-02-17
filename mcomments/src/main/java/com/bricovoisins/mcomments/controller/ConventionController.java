package com.bricovoisins.mcomments.controller;

import com.bricovoisins.mcomments.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ConventionController {
    @Autowired
    CommentDao commentDao;


}
