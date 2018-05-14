package com.moonlight.chatapp;

import android.app.Application;

import com.moonlight.chatapp.utils.SharedPreferenceUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.bmob.v3.Bmob;

/**
 * Created by songyifeng on 2018/4/5.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        SharedPreferenceUtils.initialize(instance);
        Bmob.initialize(this, getApplicationId());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public String getApplicationId() {
        Properties pro = new Properties();
        try {
            InputStream inputStream = getAssets().open("app_config.properties");
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro.getProperty("applicationid");
    }
}
