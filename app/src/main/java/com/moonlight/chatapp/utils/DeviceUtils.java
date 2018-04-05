package com.moonlight.chatapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by songyifeng on 12/09/2017.
 */

public class DeviceUtils {
    /**
     * @author:jny
     * @describe 获取mac
     * @date:2017.7,24
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface
                    .getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0"))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getLocalIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //当前使用2G/3G/4G网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                                return inetAddress.getHostAddress();
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        }
        return null;
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    // 需要检查权限
    public static String getIMEI(Context context) {
        String imei = "1234123412341234";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }

        return imei;
    }
}
