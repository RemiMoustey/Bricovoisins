package com.bricovoisins.mconventions.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Convention {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer senderId;

    private Integer recipientId;

    private String firstNameRecipient;

    private String lastNameRecipient;

    private LocalDate dateConvention;

    private LocalTime beginningHour;

    private LocalTime timeIntervention;

    private String place;

    private String phoneNumberHelped;

    private String message;

    private Boolean isValidatedByRecipient;

    private Boolean isEndedBySender;

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

    public String getFirstNameRecipient() {
        return firstNameRecipient;
    }

    public void setFirstNameRecipient(String firstNameRecipient) {
        this.firstNameRecipient = firstNameRecipient;
    }

    public String getLastNameRecipient() {
        return lastNameRecipient;
    }

    public void setLastNameRecipient(String lastNameRecipient) {
        this.lastNameRecipient = lastNameRecipient;
    }

    public LocalDate getDateConvention() {
        return dateConvention;
    }

    public void setDateConvention(LocalDate dateConvention) {
        this.dateConvention = dateConvention;
    }

    public LocalTime getBeginningHour() {
        return beginningHour;
    }

    public void setBeginningHour(LocalTime beginningHour) {
        this.beginningHour = beginningHour;
    }

    public LocalTime getTimeIntervention() {
        return timeIntervention;
    }

    public void setTimeIntervention(LocalTime timeIntervention) {
        this.timeIntervention = timeIntervention;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhoneNumberHelped() {
        return phoneNumberHelped;
    }

    public void setPhoneNumberHelped(String phoneNumberHelped) {
        this.phoneNumberHelped = phoneNumberHelped;
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

    public Boolean getEndedBySender() {
        return isEndedBySender;
    }

    public void setEndedBySender(Boolean endedBySender) {
        isEndedBySender = endedBySender;
    }

    @Override
    public String toString() {
        return "Convention{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", firstNameRecipient='" + firstNameRecipient + '\'' +
                ", lastNameRecipient='" + lastNameRecipient + '\'' +
                ", dateConvention=" + dateConvention +
                ", beginningHour=" + beginningHour +
                ", timeIntervention=" + timeIntervention +
                ", place='" + place + '\'' +
                ", phoneNumberHelped='" + phoneNumberHelped + '\'' +
                ", message='" + message + '\'' +
                ", isValidatedByRecipient=" + isValidatedByRecipient +
                ", isEndedBySender=" + isEndedBySender +
                '}';
    }
}
