package com.bricovoisins.clientui.beans;

public class CommentBean {
    public String author;

    public String comment;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
