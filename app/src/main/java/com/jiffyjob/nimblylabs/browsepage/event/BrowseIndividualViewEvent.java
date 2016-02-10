package com.jiffyjob.nimblylabs.browsePage.event;

import com.jiffyjob.nimblylabs.browsePage.BrowsePageModel;

/**
 * Created by himur on 12/28/2015.
 */
public class BrowseIndividualViewEvent {
    public BrowseIndividualViewEvent(BrowsePageModel model) {
        this.model = model;
    }

    public BrowsePageModel getModel() {
        return model;
    }

    public void setModel(BrowsePageModel model) {
        this.model = model;
    }

    private BrowsePageModel model;
}
