package com.jiffyjob.nimblylabs.postJob.jsonBuilder;

import android.location.Address;
import android.util.Log;
import android.content.Context;

import com.jiffyjob.nimblylabs.jsonUtil.IJsonBuilder;
import com.jiffyjob.nimblylabs.postJob.PostJobModel;
import com.jiffyjob.nimblylabs.xmlHelper.XmlJobCategoryHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by NimblyLabs on 17/10/2015.
 */
public class PostJobJsonBuilder implements IJsonBuilder {
    public PostJobJsonBuilder(Context context) {
        this.context = context;
    }

    @Override
    public JSONObject getJSON(Object object) {
        if (object != null && object instanceof PostJobModel) {
            try {
                PostJobModel model = (PostJobModel) object;
                postJobJson = new JSONObject();
                postJobJson.put("CreatorUserID", model.getCreatorUserId());
                postJobJson.put("UserType", model.getUserType());
                postJobJson.put("JobTitle", model.getJobTitle());
                postJobJson.put("IsGenericPhoto", model.isGenericPhotoType());

                //TODO: a list of photos or just one?
                postJobJson.put("GPhotos", model.getUserPhotos());
                postJobJson.put("UPhotos", model.getUserPhotos());

                postJobJson.put("CategoriesTitle", getCategoriesTitle(model));
                postJobJson.put("ScopesJson", getJobScopes(model));
                postJobJson.put("MinAge", model.getMinAge());
                postJobJson.put("MaxAge", model.getMaxAge());
                postJobJson.put("TotalPax", model.getTotalPax());
                //TODO:ReqMinEduLevel change to single number
                postJobJson.put("ReqMinEduLevel", model.getRequiredEducation());

                addAddress(model);

                //TODO: date formatted at UI by country
                postJobJson.put("StartDateTime", dateFormat.format(model.getStartTime().getTime()));
                postJobJson.put("EndDateTime", dateFormat.format(model.getEndTime().getTime()));

                postJobJson.put("IsSalaryDaily", model.isSalaryTypeDaily());
                postJobJson.put("SalaryCurrencyCode", model.getSalaryCurrencyCode());
                postJobJson.put("Payout", model.getPayout());
                postJobJson.put("PostBoosted", model.isBoostPost());
            } catch (JSONException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }
        return postJobJson;
    }

    @Override
    public JSONArray getJSONArray(List<Object> object) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    private JSONArray getCategoriesTitle(PostJobModel model) {
        XmlJobCategoryHelper xmlHelper = new XmlJobCategoryHelper(context);
        List<String> jobList = xmlHelper.parseXML();
        JSONArray jobCategoryJSONArray = new JSONArray();
        for (String jobCategory : model.getJobCategories()) {
            int index = jobList.indexOf(jobCategory);
            jobCategoryJSONArray.put(index);
        }
        return jobCategoryJSONArray;
    }

    private JSONArray getJobScopes(PostJobModel model) {
        JSONArray scopeJSONArray = new JSONArray();
        for (String jobScope : model.getJobScopes()) {
            scopeJSONArray.put(jobScope);
        }
        return scopeJSONArray;
    }

    private void addAddress(PostJobModel model) throws JSONException {
        Address address = model.getAddress();
        if (address != null) {
            postJobJson.put("Country", address.getCountryName());
            if (address.getAdminArea() != null) {
                postJobJson.put("State", address.getAdminArea());
            } else {
                postJobJson.put("State", address.getCountryName());
            }
            if (address.getLocality() != null) {
                postJobJson.put("City", address.getLocality());
            } else {
                postJobJson.put("City", address.getCountryName());
            }
            postJobJson.put("Lng", address.getLongitude());
            postJobJson.put("Lat", address.getLatitude());

        }
    }

    private Context context;
    private JSONObject postJobJson = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private final SimpleDateFormat dmyDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final SimpleDateFormat mdyDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
}
