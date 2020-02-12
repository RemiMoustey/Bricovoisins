package com.bricovoisins.clientui.beans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ConventionBean {

    private int id;

    private int senderId;

    private int recipientId;

    private String firstNameRecipient;

    private String lastNameRecipient;

    private LocalDate dateConvention;

    private LocalTime beginningHour;

    private LocalTime timeIntervention;

    private String place;

    private String phoneNumberHelped;

    private String message;

    private boolean isValidatedByRecipient;

    private boolean isEndedBySender;

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

    public boolean isValidatedByRecipient() {
        return isValidatedByRecipient;
    }

    public void setValidatedByRecipient(boolean validatedByRecipient) {
        isValidatedByRecipient = validatedByRecipient;
    }

    public boolean isEndedBySender() {
        return isEndedBySender;
    }

    public void setEndedBySender(boolean endedBySender) {
        isEndedBySender = endedBySender;
    }

    @Override
    public String toString() {
        return "ConventionBean{" +
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
