package com.jiffyjob.nimblylabs.updateBasicInfo;

/**
 * Created by himur on 4/24/2016.
 */
public class ProfileDetailModel {
    public ProfileDetailModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    String title;
    String content;
}
