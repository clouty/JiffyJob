package com.jiffyjob.nimblylabs.browseIndividual.Event;

import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;

/**
 * Created by NielPC on 10/14/2016.
 */
public class JobModelStickyEvent {
    public JobModelStickyEvent(JobModel jobModel) {
        this.jobModel = jobModel;
    }

    public JobModel getJobModel() {
        return jobModel;
    }

    private JobModel jobModel;
}
