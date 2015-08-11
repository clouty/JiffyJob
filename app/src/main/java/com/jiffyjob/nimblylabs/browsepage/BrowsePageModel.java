package com.jiffyjob.nimblylabs.browsePage;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.Observable;

/**
 * Created by NimblyLabs on 15/3/2015.
 */
public class BrowsePageModel extends Observable {

    public BrowsePageModel(Date date, Date[] startEndTime, String location, String role, String message, String userName, String companyName, Bitmap profileImage, String jobRating) {
        this.date = date;
        this.startEndTime = startEndTime;
        this.location = location;
        this.role = role;
        this.message = message;
        this.userName = userName;
        this.companyName = companyName;
        this.profileImage = profileImage;
        this.jobRating = jobRating;
        setChanged();
        notifyObservers();
    }

    public Date getDate() {
        return date;
    }

    public Date[] getStartEndTime() {
        return startEndTime;
    }

    public String getLocation() {
        return location;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public String getJobRating() {
        return jobRating;
    }


    public void setDate(Date date) {
        this.date = date;
        setChanged();
        notifyObservers();
    }

    public void setStartEndTime(Date[] startEndTime) {
        this.startEndTime = startEndTime;
        setChanged();
        notifyObservers();
    }

    public void setLocation(String location) {
        this.location = location;
        setChanged();
        notifyObservers();
    }

    public void setRole(String role) {
        this.role = role;
        setChanged();
        notifyObservers();
    }

    public void setMessage(String message) {
        this.message = message;
        setChanged();
        notifyObservers();
    }

    public void setUserName(String userName) {
        this.userName = userName;
        setChanged();
        notifyObservers();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
        setChanged();
        notifyObservers();
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
        setChanged();
        notifyObservers();
    }

    public void setJobRating(String jobRating) {
        this.jobRating = jobRating;
        setChanged();
        notifyObservers();
    }

    //right layout
    private Date date;
    private Date[] startEndTime; //consist of startTime and endTime
    private String location;
    private String role;
    private String jobRating;

    //left layout
    private String message;
    private String userName;
    private String companyName;
    private Bitmap profileImage;
}
