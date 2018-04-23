package com.moonlight.chatapp;

import android.app.Application;

import com.moonlight.chatapp.utils.SharedPreferenceUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by songyifeng on 2018/4/5.
 */

public class MyApplication extends Application {
    public static boolean isRelease = true;

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        SharedPreferenceUtils.initialize(instance);
    }

    public static MyApplication getInstance(){
        return instance;
    }
}
