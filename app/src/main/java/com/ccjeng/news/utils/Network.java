package com.ccjeng.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

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

    public static void AdView(Context context, MoPubView adView, String AdUnitID) {
        adView.setAdUnitId(AdUnitID);
      //  if (News.APPDEBUG) {
      //      adView.setTesting(true);
      //  }
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
    }

}
