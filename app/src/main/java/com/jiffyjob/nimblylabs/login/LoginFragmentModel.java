package com.jiffyjob.nimblylabs.login;

import android.graphics.Bitmap;

import java.util.Observable;

/**
 * Created by NimblyLabs on 29/1/2015.
 */
public class LoginFragmentModel extends Observable {
    public LoginFragmentModel(){}

    public LoginFragmentModel(String name, String gender, String location, String userID, Bitmap userProfileImage) {
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.userID = userID;
        this.userProfileImage = userProfileImage;/**/
    }

    /*Properties and variables*/
    private String name;
    private String gender;
    private String location;
    private String userID;
    private Bitmap userProfileImage;

    public Bitmap getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(Bitmap userProfileImage) {
        this.userProfileImage = userProfileImage;
        setChanged();
        notifyObservers();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        setChanged();
        notifyObservers();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        setChanged();
        notifyObservers();
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        setChanged();
        notifyObservers();
    }
}
