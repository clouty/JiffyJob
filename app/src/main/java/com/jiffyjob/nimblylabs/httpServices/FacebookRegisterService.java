package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by NimblyLabs on 10/10/2015.
 * Pass facebookId and userToken
 */
public class FacebookRegisterService extends AsyncTask<Hashtable<String, String>, Void, String> {
    public FacebookRegisterService(Context context) {
        this.context = context;
        this.urlString = context.getResources().getString(R.string.facebook_login_service);
    }

    @SafeVarargs
    @Override
    protected final String doInBackground(Hashtable<String, String>... params) {
        HttpURLConnection urlConnection = null;
        String responseStr = null;
        try {
            urlConnection = connectionUtil.getConnection(urlString);

            //Remarks: unable to BufferWrite stream data to php service
            OutputStream outStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            JSONObject jsonObject = getQuery(params[0]);
            //writer.write(URLEncoder.encode(jsonObject.toString(), "UTF-8"));
            //writer.write(jsonObject.toString());

            //without bufferwriter
            OutputStreamWriter osw = new OutputStreamWriter(outStream, "UTF-8");
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();

            responseStr = getResponse(urlConnection);
            writer.flush();
            writer.close();
            outStream.close();
        } catch (MalformedURLException e) {
            Toast.makeText(context, "Facebook register service failed.", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseStr;
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

    private String getResponse(HttpURLConnection urlConnection) {
        int responseCode;
        String response = "";
        if (urlConnection != null) {
            try {
                responseCode = urlConnection.getResponseCode();
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

    private Context context;
    private String urlString;
    private ConnectionUtil connectionUtil = new ConnectionUtil();
}
