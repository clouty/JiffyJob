package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jiffyjob.nimblylabs.httpServices.Event.ResponseEvent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import de.greenrobot.event.EventBus;

/**
 * Created by himur on 1/31/2016.
 */
public class PostHttpService extends AsyncTask<JSONObject, Integer, String> {

    public PostHttpService(String url, Object event) {
        urlString = url;
        this.event = event;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        HttpURLConnection urlConnection = null;
        String responseStr = null;

        OutputStream outStream = null;
        BufferedWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            ConnectionUtil connectionUtil = new ConnectionUtil();
            urlConnection = connectionUtil.getConnection(urlString);
            //Remarks: unable to BufferWrite stream data to php service
            outStream = urlConnection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));

            osw = new OutputStreamWriter(outStream, "UTF-8");
            osw.write(params[0].toString());
            osw.flush();
            osw.close();

            responseStr = getResponse(urlConnection);

            if (event != null && event instanceof ResponseEvent) {
                ((ResponseEvent) event).setResult(responseStr);
                postResponseEvent();
            }

            writer.flush();
            writer.close();
            outStream.close();
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseStr;
    }

    private String getResponse(HttpURLConnection urlConnection) {
        int responseCode;
        String response = "";
        if (urlConnection != null) {
            try {
                responseCode = urlConnection.getResponseCode();
                if (event != null && event instanceof ResponseEvent) {
                    ((ResponseEvent) event).setResponseCode(responseCode);
                }
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private void postResponseEvent() {
        EventBus.getDefault().post(event);
    }

    private String urlString;
    private Object event = null;
}
