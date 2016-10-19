package com.jiffyjob.nimblylabs.httpServices;

import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.managePost.model.AppliedJobModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Niellai on 7/20/2016.
 */
public interface IRetrofitJiffyAPI {

    @FormUrlEncoded
    @POST("jobService/showJob")
    Observable<List<JobModel>> showJob(@Field("content") String jsonStr);

    @FormUrlEncoded
    @POST("jobService/applyJob")
    Call<ResponseBody> applyJob(@Field("content") String jsonStr);

    @FormUrlEncoded
    @POST("jobService/appliedJobs")
    Observable<List<AppliedJobModel>> getAppliedJobs(@Field("content") String jsonStr);
}
