package com.bricovoisins.clientui.beans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ConventionBean {

    private int id;

    private int senderId;

    private String firstNameSender;

    private String lastNameSender;

    private int recipientId;

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

    private boolean isValidatedByRecipient;

    private boolean isEndedBySender;

    public ConventionBean() {
    }

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
