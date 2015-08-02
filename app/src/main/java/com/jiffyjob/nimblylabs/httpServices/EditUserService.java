package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jiffyjob.nimblylabs.updateBasicInfo.BasicInforEventModel;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by NimblyLabs on 21/6/2015.
 */
public class EditUserService extends AsyncTask<String, Void, String> {
    public EditUserService(Context context, BasicInforEventModel basicInforEventModel) {
        this.context = context;
        this.basicInforEventModel = basicInforEventModel;
    }

    @Override
    protected String doInBackground(String... params) {
        buildJSONObject();
        //executeHTTPPostRequest();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private void buildJSONObject() {
        try {
            // Adding data
            String name = basicInforEventModel.getFirstName() + " " + basicInforEventModel.getLastName();
            Date dob = basicInforEventModel.getDateOfBirth();
            String gender = basicInforEventModel.getGender();
            jsonObject = new JSONObject();
            jsonArray = new JSONArray();
            jsonObject.put("name", name);
            jsonObject.put("dob", dob);
            jsonObject.put("gender", gender);
            jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeHTTPPostRequest() {
        try {
            if (jsonObject == null || jsonArray == null) {
                return;
            }
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://nimblylabs.com/jiffyjobs_webservice/users/edituser.php");
            //Post data
            httppost.setHeader("json", jsonObject.toString());
            httppost.getParams().setParameter("jsonpost", jsonArray);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BasicInforEventModel basicInforEventModel;
    private Context context;
    private InputStream is = null;
    private String result = "";

    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
}
