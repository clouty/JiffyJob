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

    public Bitmap getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(Bitmap userPhotos) {
        this.userPhotos = userPhotos;
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

    public int getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(int totalPax) {
        this.totalPax = totalPax;
    }

    public int getRequiredEducation() {
        return requiredEducation;
    }

    public void setRequiredEducation(int requiredEducation) {
        this.requiredEducation = requiredEducation;
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

    public String getSalaryCurrencyCode() {
        return salaryCurrencyCode;
    }

    public void setSalaryCurrencyCode(String salaryCurrencyCode) {
        this.salaryCurrencyCode = salaryCurrencyCode;
    }

    public boolean isSalaryTypeDaily() {
        return isSalaryTypeDaily;
    }

    public void setSalaryTypeDaily(boolean salaryTypeDaily) {
        this.isSalaryTypeDaily = salaryTypeDaily;
    }

    public boolean isGenericPhotoType() {
        return isGenericPhotoType;
    }

    public void setIsGenericPhotoType(boolean isGenericPhotoType) {
        this.isGenericPhotoType = isGenericPhotoType;
    }

    //extended properties
    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    private String jobTitle;
    private Bitmap userPhotos;
    private List<String> jobCategories;
    private List<String> jobScopes;
    private int minAge, maxAge;
    private int totalPax;
    private int requiredEducation = -1;
    private Address address;
    private Calendar startTime;
    private Calendar endTime;
    private String salaryCurrencyCode;
    private double payout;
    private boolean isSalaryTypeDaily;
    private boolean isBoostPost;
    private boolean isAgreedToTerms;
    private boolean isGenericPhotoType;
    //extended properties
    private String creatorUserId = "1";// to be provide when login and store in device
    private String userType = "0"; //0 normal user, 1 corp user
    private String uniqueKey;
}
