package com.jiffyjob.nimblylabs.httpServices.Event;

/**
 * Created by himur on 1/31/2016.
 */
public class ResponseEvent {
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    private String result;
    private int responseCode;
}
