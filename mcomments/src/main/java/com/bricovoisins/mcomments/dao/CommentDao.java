package com.bricovoisins.mcomments.dao;

import com.bricovoisins.mcomments.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthorId(int authorId);
    List<Comment> findAllByUserIdOrderByIdDesc(int userId);
    void deleteById(int id);
}
