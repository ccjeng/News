package com.ccjeng.news;

import android.app.Application;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Logger;
//import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by andycheng on 2015/11/15.
 */
public class News extends Application {

    public static final boolean APPDEBUG = true;

    public static final String AD_MoPub = "edc19146642e4017ae2b53cb17546691";
    public static final String AD_Vpon  = "8a80818250d608d50151484e4c0a6a89";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /* Global Variables
    * */
    private static String mPrefFontSize;
    public static String getPrefFontSize(){
        return mPrefFontSize;
    }
    public static void setPrefFontSize(String s){
        mPrefFontSize = s;
    }

    private static String mPrefFontColor;
    public static String getPrefFontColor(){
        return mPrefFontColor;
    }
    public static void setPrefFontColor(String s){
        mPrefFontColor = s;
    }

    private static String mPrefBGColor;
    public static String getPrefBGColor(){
        return mPrefBGColor;
    }
    public static void setPretBGColor(String s){
        mPrefBGColor = s;
    }


    /*
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-19743390-22";
    public enum TrackerName {
        APP_TRACKER // Tracker used only in this app.
    }
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            if (APPDEBUG) {
                analytics.getInstance(this).setDryRun(true);
            }
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
    */
}
