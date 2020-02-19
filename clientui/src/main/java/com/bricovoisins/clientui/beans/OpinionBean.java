package com.bricovoisins.clientui.beans;

public class OpinionBean {
    private int id;

    public String author;

    public String opinion;

    public int userId;

    public int authorId;

    public OpinionBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "OpinionBean{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", opinion='" + opinion + '\'' +
                ", userId=" + userId +
                ", authorId=" + authorId +
                '}';
    }
}
