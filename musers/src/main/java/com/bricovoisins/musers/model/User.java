package com.bricovoisins.musers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Blob;
import java.util.Arrays;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private String firstName;

    private String lastName;

    private String password;

    private Integer age;

    private String email;

    private String town;

    private String avatar;

    private Integer points;

    private String levelGardening;

    private String levelElectricity;

    private String levelPlumbing;

    private String levelCarpentry;

    private String levelPainting;

    private String levelMasonry;

    private String levelDiy;

    private String description;

    public Boolean isAdmin;

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getLevelGardening() {
        return levelGardening;
    }

    public void setLevelGardening(String levelGardening) {
        this.levelGardening = levelGardening;
    }

    public String getLevelElectricity() {
        return levelElectricity;
    }

    public void setLevelElectricity(String levelElectricity) {
        this.levelElectricity = levelElectricity;
    }

    public String getLevelPlumbing() {
        return levelPlumbing;
    }

    public void setLevelPlumbing(String levelPlumbing) {
        this.levelPlumbing = levelPlumbing;
    }

    public String getLevelCarpentry() {
        return levelCarpentry;
    }

    public void setLevelCarpentry(String levelCarpentry) {
        this.levelCarpentry = levelCarpentry;
    }

    public String getLevelPainting() {
        return levelPainting;
    }

    public void setLevelPainting(String levelPainting) {
        this.levelPainting = levelPainting;
    }

    public String getLevelMasonry() {
        return levelMasonry;
    }

    public void setLevelMasonry(String levelMasonry) {
        this.levelMasonry = levelMasonry;
    }

    public String getLevelDiy() {
        return levelDiy;
    }

    public void setLevelDiy(String levelDiy) {
        this.levelDiy = levelDiy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", town='" + town + '\'' +
                ", avatar='" + avatar + '\'' +
                ", points=" + points +
                ", levelGardening='" + levelGardening + '\'' +
                ", levelElectricity='" + levelElectricity + '\'' +
                ", levelPlumbing='" + levelPlumbing + '\'' +
                ", levelCarpentry='" + levelCarpentry + '\'' +
                ", levelPainting='" + levelPainting + '\'' +
                ", levelMasonry='" + levelMasonry + '\'' +
                ", levelDiy='" + levelDiy + '\'' +
                ", description='" + description + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
