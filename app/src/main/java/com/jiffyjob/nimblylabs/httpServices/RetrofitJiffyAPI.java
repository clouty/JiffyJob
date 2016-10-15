package com.jiffyjob.nimblylabs.httpServices;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Niellai on 7/21/2016.
 */
public class RetrofitJiffyAPI {
    public RetrofitJiffyAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Observable<List<JobModel>> showJob(@NonNull String jsonStr) {
        IRetrofitJiffyAPI retrofitJiffyAPI = retrofit.create(IRetrofitJiffyAPI.class);
        return retrofitJiffyAPI.showJob(jsonStr);
    }

    public Call<ResponseBody> applyJob(@NonNull String jsonStr) {
        IRetrofitJiffyAPI retrofitJiffyAPI = retrofit.create(IRetrofitJiffyAPI.class);
        return retrofitJiffyAPI.applyJob(jsonStr);
    }

    private Retrofit retrofit;
    private final String url = "http://www.nimblylabs.com/jjws/v1/";
}
