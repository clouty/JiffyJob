package com.jiffyjob.nimblylabs.managePost.model;

/**
 * Created by NielPC on 10/16/2016.
 */
public class AppliedJobModel {

    public AppliedJobModel(String jobID, int status) {
        this.JobID = jobID;
        this.Status = status;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        this.JobID = jobID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    private String JobID;
    private int Status;
}
