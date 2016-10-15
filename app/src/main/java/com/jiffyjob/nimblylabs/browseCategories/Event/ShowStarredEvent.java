package com.jiffyjob.nimblylabs.browseCategories.Event;

/**
 * Created by NielPC on 10/7/2016.
 */
public class ShowStarredEvent {
    public ShowStarredEvent(boolean isShowStarred) {
        this.isShowStarred = isShowStarred;
    }

    public boolean isShowStarred() {
        return isShowStarred;
    }

    private boolean isShowStarred;
}
