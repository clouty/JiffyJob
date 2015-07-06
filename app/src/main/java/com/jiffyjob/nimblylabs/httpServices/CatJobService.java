package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jiffyjob.nimblylabs.commonUtilities.ASyncResponse;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 31/5/2015.
 */
public class CatJobService extends AsyncTask<String, Void, String> {

    public CatJobService(Context context,String category, ASyncResponse asyncResponse ) {
        this.delegate = asyncResponse;
        this.context = context;
        this.category = category;
    }

    @Override
    protected String doInBackground(String... params) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.nimblylabs.com/jiffyjobs_webservice/jobs/fetchcategoryjob.php");

        try {
            JSONObject data = new JSONObject();
            data.put("Category", category);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(data);

            // Post the data:
            httppost.setHeader("json", data.toString());
            httppost.getParams().setParameter("jsonpost", jsonArray);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }  catch (JSONException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }catch (ClientProtocolException e) {
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(result);
    }


    private Context context;
    private InputStream is = null;
    private String category = "";
    private String result = "";
    public ASyncResponse delegate = null;
}

