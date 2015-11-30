package com.ccjeng.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Network {

    private static ConnectivityManager connMgr;

    public static boolean isNetworkConnected(Context context){
        if(null == connMgr){
            connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isWifiAvailable(Context context) {
        if(null == connMgr) {
            connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        if (connMgr.getActiveNetworkInfo().isAvailable())
            return true;
        else
            return false;
    }

}
