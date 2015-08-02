package com.jiffyjob.nimblylabs.postJob;

import android.graphics.Bitmap;
import android.location.Address;

import java.util.Calendar;
import java.util.List;

/**
 * Created by NimblyLabs on 19/7/2015.
 */
public class PostJobModel {
    public PostJobModel() {
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public List<String> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<String> jobCategories) {
        this.jobCategories = jobCategories;
    }

    public List<String> getJobScopes() {
        return jobScopes;
    }

    public void setJobScopes(List<String> jobScopes) {
        this.jobScopes = jobScopes;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getHiringPax() {
        return hiringPax;
    }

    public void setHiringPax(int hiringPax) {
        this.hiringPax = hiringPax;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public double getPayout() {
        return payout;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }

    public boolean isBoostPost() {
        return isBoostPost;
    }

    public void setIsBoostPost(boolean isBoostPost) {
        this.isBoostPost = isBoostPost;
    }

    public boolean isAgreedToTerms() {
        return isAgreedToTerms;
    }

    public void setIsAgreedToTerms(boolean isAgreedToTerms) {
        this.isAgreedToTerms = isAgreedToTerms;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    private String jobTitle;
    private Bitmap userImage;
    private List<String> jobCategories;
    private List<String> jobScopes;
    private int minAge, maxAge;
    private int hiringPax;
    private List<String> hashtags;
    private Address address;
    private Calendar startTime;
    private Calendar endTime;
    private double payout;
    private boolean isBoostPost;
    private boolean isAgreedToTerms;
}
