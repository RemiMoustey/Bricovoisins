package com.bricovoisins.clientui.beans;

import java.util.Date;

public class ConventionBean {

    private int id;

    private int senderId;

    private int recipientId;

    private Date dateConvention;

    private String message;

    private boolean isValidatedByRecipient;

    private boolean isValidatedBySender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public Date getDateConvention() {
        return dateConvention;
    }

    public void setDateConvention(Date dateConvention) {
        this.dateConvention = dateConvention;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isValidatedByRecipient() {
        return isValidatedByRecipient;
    }

    public void setValidatedByRecipient(boolean validatedByRecipient) {
        isValidatedByRecipient = validatedByRecipient;
    }

    public boolean isValidatedBySender() {
        return isValidatedBySender;
    }

    public void setValidatedBySender(boolean validatedBySender) {
        isValidatedBySender = validatedBySender;
    }

    @Override
    public String toString() {
        return "ConventionBean{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", dateConvention=" + dateConvention +
                ", message='" + message + '\'' +
                ", isValidatedByRecipient=" + isValidatedByRecipient +
                ", isValidatedBySender=" + isValidatedBySender +
                '}';
    }
}
