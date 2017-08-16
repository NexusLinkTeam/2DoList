package com.jacob.www.a2dolist.mvp.model.Impl;

/**
 * Created by 猿人 on 2017/5/24.
 */


import com.jacob.www.a2dolist.net.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * 该类主要提供网络请求所需要的service和进行状态码的过滤
 */
public class BaseModelImpl {
    private OkHttpClient client = new OkHttpClient.Builder()
            .build();
    private Retrofit mRetrofit;
    protected Api api;

    public BaseModelImpl() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = mRetrofit.create(Api.class);
    }
}
