package com.ccjeng.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Network {

    private static ConnectivityManager connMgr;

    public static boolean isNetworkAvailable(Context context){
        if(null == connMgr){
            connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiInfo.isAvailable()){
            return true;
        }else if(mobileInfo.isAvailable()){
            return true;
        }else{
            return false;
        }
    }
}
