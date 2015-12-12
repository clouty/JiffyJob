package com.jiffyjob.nimblylabs.browsePage;

import android.graphics.drawable.BitmapDrawable;

import java.util.Date;
import java.util.Observable;

/**
 * Created by NimblyLabs on 15/3/2015.
 */
public class BrowsePageModel extends Observable {
    public BrowsePageModel(String title, String jobID, Date jobDate, Date[] startEndTime, String location, String jobPax, String creatorName, String creatorCompanyName, BitmapDrawable jobLogo, String creatorProfileImageUrl, String bgBitmapDrawableUrl) {
        this.title = title;
        this.jobID = jobID;
        this.jobDate = jobDate;
        this.startEndTime = startEndTime;
        this.location = location;
        this.jobPax = jobPax;
        this.creatorName = creatorName;
        this.creatorCompanyName = creatorCompanyName;
        this.jobLogo = jobLogo;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.bgBitmapDrawableUrl = bgBitmapDrawableUrl;
        setChanged();
        notifyObservers();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setChanged();
        notifyObservers();
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
        setChanged();
        notifyObservers();
    }

    public Date getJobDate() {
        return jobDate;
    }

    public void setJobDate(Date jobDate) {
        this.jobDate = jobDate;
        setChanged();
        notifyObservers();
    }

    public Date[] getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(Date[] startEndTime) {
        this.startEndTime = startEndTime;
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

    public String getJobPax() {
        return jobPax;
    }

    public void setJobPax(String jobPax) {
        this.jobPax = jobPax;
        setChanged();
        notifyObservers();
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        setChanged();
        notifyObservers();
    }

    public String getCreatorCompanyName() {
        return creatorCompanyName;
    }

    public void setCreatorCompanyName(String creatorCompanyName) {
        this.creatorCompanyName = creatorCompanyName;
        setChanged();
        notifyObservers();
    }

    public String getJobRating() {
        return jobRating;
    }

    public void setJobRating(String jobRating) {
        this.jobRating = jobRating;
    }

    public BitmapDrawable getJobLogo() {
        return jobLogo;
    }

    public void setJobLogo(BitmapDrawable jobLogo) {
        this.jobLogo = jobLogo;
        setChanged();
        notifyObservers();
    }

    public String getCreatorProfileImageUrl() {
        return creatorProfileImageUrl;
    }

    public void setCreatorProfileImageUrl(String creatorProfileImageUrl) {
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        setChanged();
        notifyObservers();
    }

    public String getBgBitmapDrawableUrl() {
        return bgBitmapDrawableUrl;
    }

    public void setBgBitmapDrawableUrl(String bgBitmapDrawableUrl) {
        this.bgBitmapDrawableUrl = bgBitmapDrawableUrl;
        setChanged();
        notifyObservers();
    }

    //Job quick information
    private String title;
    private String jobID;
    private Date jobDate;
    private Date[] startEndTime;
    private String location;
    private String jobPax;
    private String jobRating;

    //Job title and creator info
    private String creatorName;
    private String creatorCompanyName;

    //Images required
    private BitmapDrawable jobLogo;
    private String creatorProfileImageUrl;
    private String bgBitmapDrawableUrl;
}
