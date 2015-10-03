package com.jiffyjob.nimblylabs.beforeLoginFragmentViews;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by NimblyLabs on 27/9/2015.
 */
public class UserInfoModel {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public int getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(int highestQualification) {
        this.highestQualification = highestQualification;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    private String userName;
    private String password;
    private String email;
    private Date dob;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    private String userLocation;
    private String contactNo;
    private int highestQualification;
    private String shortBio;
    private Bitmap userPhoto;
}
