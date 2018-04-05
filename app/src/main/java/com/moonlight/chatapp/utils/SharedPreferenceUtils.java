package com.moonlight.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SharedPreferenceUtils {

    private static final String PREF_NAME = "app_cache";

    private static SharedPreferences pref;

    private static SharedPreferences.Editor editor;

    public static void initialize(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public synchronized static boolean contains(String key) {
        return pref.contains(key);
    }

    public synchronized static boolean get(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public synchronized static float get(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public synchronized static int get(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public synchronized static long get(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public synchronized static String get(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public synchronized static void put(String key, boolean value) {
        editor.putBoolean(key, value);
        commit();
    }

    public synchronized static void put(String key, float value) {
        editor.putFloat(key, value);
        commit();
    }

    public synchronized static void put(String key, int value) {
        editor.putInt(key, value);
        commit();
    }

    public synchronized static void put(String key, long value) {
        editor.putLong(key, value);
        commit();
    }

    public synchronized static void put(String key, String value) {
        editor.putString(key, value);
        commit();
    }

    public synchronized static void commit() {
        editor.commit();
    }

    public synchronized static void clear() {
        editor.clear();
        commit();
    }

    public synchronized static void remove(String key) {
        editor.remove(key);
        commit();
    }
}

