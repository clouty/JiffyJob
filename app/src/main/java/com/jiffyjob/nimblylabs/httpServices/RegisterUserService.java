package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;

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
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * Created by NimblyLabs on 31/5/2015.
 */
public class RegisterUserService extends AsyncTask<Hashtable<String, String>, Void, String> {

    public RegisterUserService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Hashtable<String, String>... params) {
        // Create a new HttpClient and Post Header
        String urlString = context.getResources().getString(R.string.facebook_login_service);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlString);

        try {
            // Add your data
            JSONObject data = getQuery(params[0]);

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
                System.out.println(result);
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //TODO: notify account successfully created, redirect user to main page
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    private JSONObject getQuery(Hashtable<String, String> params) throws UnsupportedEncodingException {
        JSONObject jsonMsg = new JSONObject();
        try {
            for (String key : params.keySet()) {
                jsonMsg.put(key, params.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonMsg;
    }

    private Context context;
    private InputStream is = null;
    private String result = "";
}
