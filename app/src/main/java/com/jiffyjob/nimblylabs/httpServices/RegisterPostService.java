package com.jiffyjob.nimblylabs.httpServices;

import android.os.AsyncTask;
import android.util.Log;

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
public class RegisterPostService extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        for (String para : params) {
            registerList.add(para);
        }
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://nimblylabs.com/jiffyjobs_webservice/users/insertuser.php");

        try {
            // Add your data
            JSONObject data = new JSONObject();
            data.put("Email", registerList.get(0));
            data.put("Password", registerList.get(1));

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
                String text = sb.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        return null;
    }

    private List<String> registerList = new ArrayList<>();
    private InputStream is = null;
}
