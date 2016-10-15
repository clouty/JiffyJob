package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NimblyLabs on 20/6/2015.
 */
public class BasicInforEventModel {
    public BasicInforEventModel() {
        this.interestList = new ArrayList<>();
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }

    public List<String> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<String> interestList) {
        this.interestList = interestList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(List<Integer> badgeList) {
        this.badgeList = badgeList;
    }

    private List<Integer> badgeList;
    private Bitmap userImage;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String highestQualification;
    private List<String> interestList;
    private String description = "";
}
