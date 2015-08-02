package com.jiffyjob.nimblylabs.postJob.PostJobEvents;

import com.jiffyjob.nimblylabs.postJob.PostJobModel;

/**
 * Created by NimblyLabs on 26/7/2015.
 */
public class PostJobStep4Event {
    public PostJobStep4Event(PostJobModel model) {
        postJobModel = model;
    }

    public PostJobModel getPostJobModel() {
        return postJobModel;
    }

    public void setPostJobModel(PostJobModel postJobModel) {
        this.postJobModel = postJobModel;
    }

    private PostJobModel postJobModel;
}
