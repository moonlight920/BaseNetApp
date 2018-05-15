package com.moonlight.chatapp.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by songyifeng on 2018/5/15.
 */

public class Constants {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.moonlight";

    public static final String TMP_IMG_PATH = ROOT_PATH + File.separator + "tempImg";
}
