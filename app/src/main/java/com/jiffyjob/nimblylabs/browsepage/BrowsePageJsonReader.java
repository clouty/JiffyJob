package com.jiffyjob.nimblylabs.browsePage;

import android.content.Context;
import android.util.Log;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by himur on 12/25/2015.
 */
public class BrowsePageJsonReader {
    public BrowsePageJsonReader(JSONArray jsonArray, Context context) {
        this.context = context;
        generateEduList();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                BrowsePageModel model = new BrowsePageModel();
                JSONObject element = (JSONObject) jsonArray.get(i);
                model.setId1(element.getString("ID1"));
                model.setId2(element.getString("ID2"));
                model.setId3(element.getString("ID3"));
                model.setCorpProfile(element.getString("CorporateProfile"));
                model.setCompanyName(element.getString("CompanyName"));
                model.setFirstName(element.getString("COALESCE(UserProfile.FirstName,'-')"));
                model.setLastName(element.getString("COALESCE(UserProfile.LastName,'-')"));
                model.setScopes(element.getString("ScopesJson"));
                model.setJobTitle(element.getString("JobTitle"));
                try {
                    int userType = Integer.parseInt(element.getString("UserType"));
                    model.setUserType(userType);
                    int minAge = Integer.parseInt(element.getString("MinAge"));
                    model.setMinAge(minAge);
                    int maxAge = Integer.parseInt(element.getString("MaxAge"));
                    model.setMaxAge(maxAge);
                    int totalPax = Integer.parseInt(element.getString("TotalPax"));
                    model.setTotalPax(totalPax);
                    double lat = Double.parseDouble(element.getString("Lat"));
                    model.setLatitude(lat);
                    double lng = Double.parseDouble(element.getString("Lng"));
                    model.setLongitude(lng);
                    int totalWorkingDays = Integer.parseInt(element.getString("TotalWorkDays"));
                    model.setTotalWorkDays(totalWorkingDays);
                    double payout = Double.parseDouble(element.getString("Payout"));
                    model.setPayout(payout);
                    int currentlyRecruited = Integer.parseInt(element.getString("CurrentlyRecruited"));
                    model.setCurrentlyRecuited(currentlyRecruited);
                    int jobStatus = Integer.parseInt(element.getString("JobStatus"));
                    model.setJobStatus(jobStatus);
                    int isSalaryDaily = Integer.parseInt(element.getString("IsSalaryDaily"));
                    if (isSalaryDaily == 0) {
                        model.setIsSalaryDaily(false);
                    } else {
                        model.setIsSalaryDaily(true);
                    }
                    int boostPost = Integer.parseInt(element.getString("PostBoosted"));
                    if (boostPost == 0) {
                        model.setIsPostBoosted(false);
                    } else {
                        model.setIsPostBoosted(true);
                    }
                    int index = Integer.parseInt(element.getString("ReqMinEduLevel"));
                    model.setReqMinEduLevel(educationList.get(index));
                } catch (NumberFormatException e) {
                    Log.e(BrowsePageJsonReader.class.getSimpleName(), e.getMessage());
                }

                String uPhoto = element.getString("UPhotoDetails");
                String gPhoto = element.getString("GPhotoDetails");
                if (uPhoto != null && !uPhoto.isEmpty()) {
                    model.setBitmapUri(uPhoto);
                } else {
                    model.setBitmapUri(gPhoto);
                }

                model.setState(element.getString("State"));
                model.setCity(element.getString("City"));
                model.setCurrencyCode(element.getString("SalaryCurrencyCode"));

                try {
                    Date datePosted = dateFormat.parse(element.getString("DatePosted"));
                    model.setDatePosted(datePosted);
                    Date lastModify = dateFormat.parse(element.getString("LastModify"));
                    model.setLastModified(lastModify);
                    Date startDateTime = dateFormat.parse(element.getString("StartDateTime"));
                    model.setStartDate(startDateTime);
                    Date endDateTime = dateFormat.parse(element.getString("EndDateTime"));
                    model.setEndDate(endDateTime);
                } catch (ParseException e) {
                    Log.e(BrowsePageJsonReader.class.getSimpleName(), e.getMessage());
                }

                modelList.add(model);
            } catch (JSONException e) {
                Log.e(BrowsePageJsonReader.class.getSimpleName(), e.getMessage());
            }
        }
    }

    public ArrayList<BrowsePageModel> getModelList() {
        return modelList;
    }

    private void generateEduList() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.qualitfication_global);
        educationList = simpleXMLReader.parseXML();
    }

    private Context context;
    private List<String> educationList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private ArrayList<BrowsePageModel> modelList = new ArrayList<>();
}

