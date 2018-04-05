package com.moonlight.chatapp.net;

import com.moonlight.chatapp.MyApplication;
import com.moonlight.chatapp.net.CustomGsonConverter.CustomGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Administrator on 2017/7/24.
 */

public class RetrofitFactory {

    private static final String DEVELOP_URL = "http://www.moonlight.com/";
    private static final String TEST_URL = "http://www.moonlight.com/";
    private static final String RELEASE_URL = "http://www.moonlight.com/";

    private static String BASE_URL = TEST_URL;


    private static final long TIMEOUT = 10;

    private RetrofitFactory() {

    }

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new CommonInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitService retrofitService ;

    public static RetrofitService getInstance() {
        if(retrofitService == null){

            BASE_URL = MyApplication.isRelease ? RELEASE_URL : TEST_URL;

            retrofitService = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(RetrofitService.class);
        }
        return retrofitService;
    }
}
