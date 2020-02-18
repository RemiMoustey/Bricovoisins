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

    private String firstNameSender;

    private String lastNameSender;

    private Integer recipientId;

    private String firstNameRecipient;

    private String lastNameRecipient;

    private LocalDate dateConvention;

    private LocalDate dateBeginning;

    private LocalDate dateEndConvention;

    private LocalTime beginningHour;

    private LocalTime timeIntervention;

    private String place;

    private String phoneNumberHelped;

    private String message;

    private Boolean isValidatedByRecipient;

    private Boolean isEndedBySender;

    public Convention() {
    }

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

    public String getFirstNameSender() {
        return firstNameSender;
    }

    public void setFirstNameSender(String firstNameSender) {
        this.firstNameSender = firstNameSender;
    }

    public String getLastNameSender() {
        return lastNameSender;
    }

    public void setLastNameSender(String lastNameSender) {
        this.lastNameSender = lastNameSender;
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

    public LocalDate getDateBeginning() {
        return dateBeginning;
    }

    public void setDateBeginning(LocalDate dateBeginning) {
        this.dateBeginning = dateBeginning;
    }

    public LocalDate getDateEndConvention() {
        return dateEndConvention;
    }

    public void setDateEndConvention(LocalDate dateEndConvention) {
        this.dateEndConvention = dateEndConvention;
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
                ", firstNameSender='" + firstNameSender + '\'' +
                ", lastNameSender='" + lastNameSender + '\'' +
                ", recipientId=" + recipientId +
                ", firstNameRecipient='" + firstNameRecipient + '\'' +
                ", lastNameRecipient='" + lastNameRecipient + '\'' +
                ", dateConvention=" + dateConvention +
                ", dateBeginning=" + dateBeginning +
                ", dateEndConvention=" + dateEndConvention +
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
