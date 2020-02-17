package com.bricovoisins.mcomments.dao;

import com.bricovoisins.mcomments.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {

}
