package com.jiffyjob.nimblylabs.httpServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 18/10/2015.
 */
public class ConnectionUtil {
    /**
     * Method ensure all http connection are setup the same.
     *
     * @param urlString URL string that http will attempt to connect.
     * @return Return http url connection.
     * @throws IOException throws exception when unable to connect.
     */
    public HttpURLConnection getConnection(String urlString) throws IOException {
        this.url = new URL(urlString);
        this.urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Host", "nimblylabs.com");
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
        urlConnection.connect();
        return urlConnection;
    }

    public HttpURLConnection getConnFormData(String urlString) throws IOException {
        this.url = new URL(urlString);
        this.urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
        return urlConnection;
    }

    private URL url;
    private HttpURLConnection urlConnection;
}
