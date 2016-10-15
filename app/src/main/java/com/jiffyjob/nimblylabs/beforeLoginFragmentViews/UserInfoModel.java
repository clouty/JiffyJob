package com.jiffyjob.nimblylabs.beforeLoginFragmentViews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by NimblyLabs on 27/9/2015.
 */
public class UserInfoModel {

    public UserInfoModel(JSONObject jsonObject, String type) {
        this.jsonObject = jsonObject;
        if (type.equalsIgnoreCase("LINKEDIN_USER_INFO")) {
            storeLinkedInJson();
        } else if (type.equalsIgnoreCase("FACEBOOK_USER_INFO")) {
            storeFacebookJson();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    private void storeLinkedInJson() {
        try {
            if (jsonObject.has("id")) {
                this.setId(jsonObject.getString("id"));
            }
            if (jsonObject.has("emailAddress")) {
                this.setEmail(jsonObject.getString("emailAddress"));
            }
            if (jsonObject.has("firstName")) {
                this.setFirstName(jsonObject.getString("firstName"));
            }
            if (jsonObject.has("lastName")) {
                this.setLastName(jsonObject.getString("lastName"));
            }
            if (jsonObject.has("industry")) {
                this.setInterest(jsonObject.getString("industry"));
            }
            if (jsonObject.has("location")) {
                JSONObject locationObject = jsonObject.getJSONObject("location");
                if (locationObject.has("name")) {
                    this.setUserLocation(locationObject.getString("name"));
                }
            }
            if (jsonObject.has("pictureUrls")) {
                JSONObject picObject = jsonObject.getJSONObject("pictureUrls");
                int count = picObject.getInt("_total");
                if (count > 0 && picObject.has("values")) {
                    JSONArray picJsonArray = picObject.getJSONArray("values");
                    this.setUserPhoto(picJsonArray.getString(0));
                }
            }
            if (jsonObject.has("summary")) {
                this.setShortBio(jsonObject.getString("summary"));
            }
        } catch (JSONException e) {
            Log.e(UserInfoModel.class.getSimpleName(), "Error: " + e.getMessage());
        }
    }

    private void storeFacebookJson() {
        try {
            JSONObject picObject = jsonObject.getJSONObject("picture");
            JSONObject dataObject = picObject.getJSONObject("data");
            String profilePicUrl = dataObject.getString("url");

            JSONObject coverObject = jsonObject.getJSONObject("cover");
            String sourceUrl = coverObject.getString("source");

            JSONObject locationObject = jsonObject.getJSONObject("location");
            String locationStr = locationObject.getString("name");

            String fbID = jsonObject.getString("id");
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String email = jsonObject.getString("email");
            String birthday = jsonObject.getString("birthday");
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            /*DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);*/
            Date dob = format.parse(birthday);
            String gender = jsonObject.getString("gender");

          /*Glide from url to bitmap
            Bitmap profilePic = Glide.with(getApplicationContext())
                    .load(profilePicUrl)
                    .asBitmap()
                    .into(-1, -1)
                    .get();

            Bitmap coverPic = Glide.with(getApplicationContext())
                    .load(sourceUrl)
                    .asBitmap()
                    .into(-1, -1)
                    .get();*/
            this.setUserPhoto(profilePicUrl);
            this.setCoverPic(sourceUrl);
            this.setUserLocation(locationStr);
            this.setEmail(email);
            this.setId(fbID);
            this.setFirstName(firstName);
            this.setLastName(lastName);
            this.setDob(dob);
            this.setGender(gender);
        } catch (JSONException e) {
            Log.e(UserInfoModel.class.getSimpleName(), "Error: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> badgeList;

    private JSONObject jsonObject;
    private String id;
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
    private String userPhoto;
    private String coverPic;
    private String interest;
}
