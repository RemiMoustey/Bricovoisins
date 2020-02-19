package com.bricovoisins.mopinions.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Opinion {
    @Id
    @GeneratedValue
    private Integer id;

    private String author;
    
    private String opinion;

    private Integer userId;

    private Integer authorId;

    public Opinion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", opinion='" + opinion + '\'' +
                ", userId=" + userId +
                ", authorId=" + authorId +
                '}';
    }
}
