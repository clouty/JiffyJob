package com.jiffyjob.nimblylabs.postJob.PostJobEvents;

import com.jiffyjob.nimblylabs.postJob.PostJobModel;

/**
 * Created by NimblyLabs on 25/7/2015.
 */
public class PostJobStep3Event {
    public PostJobStep3Event(PostJobModel model) {
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
