package com.moonlight.chatapp.net;

import com.moonlight.chatapp.bean.BaseResponse;
import com.moonlight.chatapp.bean.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/7/24.
 */

public interface RetrofitService {

    @GET("api")
    Observable<BaseResponse<User>> login(@Query("name") String name);
}
