package com.ccjeng.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Network {

    private final static String TAG = Network.class.getName();

    private static ConnectivityManager connMgr;

    public static boolean isNetworkConnected(Context context) {
        if (null == connMgr) {
            connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isWifiAvailable(Context context) {
        if (null == connMgr) {
            connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
            if (networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static void AdView(MoPubView adView, String AdUnitID) {
        adView.setAdUnitId(AdUnitID);
        //if (!BaseApplication.APPDEBUG) {
            //adView.setTesting(true);

            adView.loadAd();
            adView.setBannerAdListener(new MoPubView.BannerAdListener() {
                @Override
                public void onBannerLoaded(MoPubView banner) {

                }

                @Override
                public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {

                }

                @Override
                public void onBannerClicked(MoPubView banner) {

                }

                @Override
                public void onBannerExpanded(MoPubView banner) {

                }

                @Override
                public void onBannerCollapsed(MoPubView banner) {

                }
            });
       // }
    }


    public static String checkNewsViewURL(String url) {

        //force change TW / HK yahoo url to mobile version.
        if (url.contains("news.yahoo.com")) {

            url = url.replace("news", "mobi");

            Log.d(TAG, "New URL =" + url);

        }

        return url;

    }
}
