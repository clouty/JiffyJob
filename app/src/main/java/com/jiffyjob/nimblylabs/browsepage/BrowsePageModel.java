package com.jiffyjob.nimblylabs.browsePage;

import java.util.Date;
import java.util.Observable;

/**
 * Created by NimblyLabs on 15/3/2015.
 */
public class BrowsePageModel extends Observable {
    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getCorpProfile() {
        return corpProfile;
    }

    public void setCorpProfile(String corpProfile) {
        this.corpProfile = corpProfile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getReqMinEduLevel() {
        return reqMinEduLevel;
    }

    public void setReqMinEduLevel(String reqMinEduLevel) {
        this.reqMinEduLevel = reqMinEduLevel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTotalWorkDays() {
        return totalWorkDays;
    }

    public void setTotalWorkDays(int totalWorkDays) {
        this.totalWorkDays = totalWorkDays;
    }

    public boolean isSalaryDaily() {
        return isSalaryDaily;
    }

    public void setIsSalaryDaily(boolean isSalaryDaily) {
        this.isSalaryDaily = isSalaryDaily;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getPayout() {
        return payout;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }

    public int getCurrentlyRecuited() {
        return currentlyRecuited;
    }

    public void setCurrentlyRecuited(int currentlyRecuited) {
        this.currentlyRecuited = currentlyRecuited;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public boolean isPostBoosted() {
        return isPostBoosted;
    }

    public void setIsPostBoosted(boolean isPostBoosted) {
        this.isPostBoosted = isPostBoosted;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getBitmapUri() {
        return bitmapUri;
    }

    public void setBitmapUri(String bitmapUri) {
        this.bitmapUri = bitmapUri;
    }

    //Model properties
    //incrementalID, JobID, CreatorID
    private String id1, id2, id3;
    private String corpProfile, companyName;
    private String FirstName, LastName;
    private int userType;
    private String scopes;
    private Date datePosted;
    private String jobTitle;
    private int minAge, maxAge;
    private int totalPax;
    private String reqMinEduLevel;
    private String country, state, city;
    private double longitude, latitude;
    private Date startDate, endDate;
    private int totalWorkDays;
    private boolean isSalaryDaily;
    private String currencyCode;
    private double payout;
    private int currentlyRecuited;
    private int jobStatus;
    private boolean isPostBoosted;
    private Date lastModified;

    //Image uri
    private String bitmapUri;
}
