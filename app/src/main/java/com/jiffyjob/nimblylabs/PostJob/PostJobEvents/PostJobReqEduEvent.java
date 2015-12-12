package com.jiffyjob.nimblylabs.postJob.postJobEvents;

/**
 * Created by NimblyLabs on 12/10/2015.
 */
public class PostJobReqEduEvent {

    public PostJobReqEduEvent(String reqEducation) {
        this.reqEducation = reqEducation;
    }

    public String getReqEducation() {
        return reqEducation;
    }

    public void setReqEducation(String reqEducation) {
        this.reqEducation = reqEducation;
    }

    private String reqEducation = null;
}
