package com.jiffyjob.nimblylabs.postJob.postJobEvents;

/**
 * Created by NimblyLabs on 25/7/2015.
 */
public class ScopeItemEvent {
    public ScopeItemEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
}
