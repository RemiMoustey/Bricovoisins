package com.bricovoisins.mconventions.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Convention {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer senderId;

    private Integer recipientId;

    private Date dateConvention;

    private String message;

    private Boolean isValidatedByRecipient;

    private Boolean isValidatedBySender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
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

    public Boolean getValidatedByRecipient() {
        return isValidatedByRecipient;
    }

    public void setValidatedByRecipient(Boolean validatedByRecipient) {
        isValidatedByRecipient = validatedByRecipient;
    }

    public Boolean getValidatedBySender() {
        return isValidatedBySender;
    }

    public void setValidatedBySender(Boolean validatedBySender) {
        isValidatedBySender = validatedBySender;
    }

    @Override
    public String toString() {
        return "Convention{" +
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
