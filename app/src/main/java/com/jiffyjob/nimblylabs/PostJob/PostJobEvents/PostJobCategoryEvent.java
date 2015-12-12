package com.jiffyjob.nimblylabs.postJob.postJobEvents;

import java.util.List;

/**
 * Created by NimblyLabs on 20/7/2015.
 */
public class PostJobCategoryEvent {
    public PostJobCategoryEvent(List<String> postJobCategory) {
        this.postJobCategory = postJobCategory;
    }

    public List<String> getPostJobCategory() {
        return postJobCategory;
    }

    public void setPostJobCategory(List<String> postJobCategory) {
        this.postJobCategory = postJobCategory;
    }

    private List<String> postJobCategory;
}
