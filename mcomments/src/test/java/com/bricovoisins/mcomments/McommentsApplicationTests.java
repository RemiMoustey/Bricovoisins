package com.bricovoisins.mcomments;

import com.bricovoisins.mcomments.controller.CommentController;
import com.bricovoisins.mcomments.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class McommentsApplicationTests {

	@Autowired
	CommentController commentController;

	@Test
	void testInsertComment() {
		int previousSize = commentController.getAllComments().size();
		Comment newComment = new Comment();
		newComment.setAuthor("Rémi Moustey");
		newComment.setComment("Commentaire");
		newComment.setUserId(63);
		newComment.setAuthorId(62);
		commentController.insertComment(newComment);
		assertEquals(previousSize + 1, commentController.getAllComments().size());
		Comment addedComment = commentController.getAllCommentsByAuthor(62).get(1);
		commentController.deleteComment(addedComment.getId());
	}

	@Test
	void testGetAllCommentsByUser() {
		assertEquals(1, commentController.getAllCommentsByAuthor(62).size());
	}

	@Test
	void testGetComment() {
		Comment newComment = new Comment();
		newComment.setAuthor("Rémi Moustey");
		newComment.setComment("Commentaire");
		newComment.setUserId(63);
		newComment.setAuthorId(62);
		commentController.insertComment(newComment);
		Comment comment = commentController.getAllCommentsByAuthor(62).get(0);
		assertEquals("Commentaire", commentController.getComment(comment.getId()).getComment());
		commentController.deleteComment(comment.getId());
	}

	@Test
	void testDeleteComment() {
		Comment newComment = new Comment();
		newComment.setAuthor("Rémi Moustey");
		newComment.setComment("Commentaire");
		newComment.setUserId(63);
		newComment.setAuthorId(62);
		commentController.insertComment(newComment);
		Comment addedComment = commentController.getAllCommentsByAuthor(62).get(1);
		int previousSize = commentController.getAllComments().size();
		commentController.deleteComment(addedComment.getId());
		assertEquals(previousSize - 1, commentController.getAllComments().size());
	}
}
