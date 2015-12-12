package com.jiffyjob.nimblylabs.httpServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.IASyncResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by NimblyLabs on 31/5/2015.
 */
public class QueryJobService extends AsyncTask<String, Void, String> {

    public QueryJobService(Context context, IASyncResponse asyncResponse) {
        this.delegate = asyncResponse;
        this.context = context;
        this.urlString = context.getResources().getString(R.string.queryJob_service);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String responseStr = null;
        try {
            urlConnection = connectionUtil.getConnection(urlString);
            //Remarks: unable to BufferWrite stream data to php service
            OutputStream outStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));

            //params[0] provide query string
            OutputStreamWriter osw = new OutputStreamWriter(outStream, "UTF-8");
            osw.write(params[0].toString());
            osw.flush();
            osw.close();

            responseStr = getResponse(urlConnection);
            result = getJson(responseStr);
            writer.flush();
            writer.close();
            outStream.close();
        } catch (MalformedURLException e) {
            Toast.makeText(context, "Post job service failed.", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(result);
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

    //TODO: output from server should be pure JSON and compressed
    private String getJson(String inputStr) {
        String result = inputStr.substring(inputStr.indexOf("Result: ") + 8);
        result = result.replace("</body></html>","");
        return result;
    }

    private Context context;
    private InputStream is = null;
    private String result = "";
    private String urlString;
    public IASyncResponse delegate = null;
    private ConnectionUtil connectionUtil = new ConnectionUtil();
}

