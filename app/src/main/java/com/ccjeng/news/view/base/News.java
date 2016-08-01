package com.ccjeng.news.view.base;

import android.app.Application;

import com.ccjeng.news.BuildConfig;
import com.ccjeng.news.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.yolanda.nohttp.NoHttp;

import java.util.HashMap;

import me.majiajie.swipeback.utils.ActivityStack;

/**
 * Created by andycheng on 2015/11/15.
 */
public class News extends Application {

    public static final boolean APPDEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        NoHttp.initialize(this);
        this.registerActivityLifecycleCallbacks(ActivityStack.getInstance());
    }

    /* Global Variables
    * */
    private static String mPrefFontSize = "";
    public static String getPrefFontSize(){
        return mPrefFontSize;
    }
    public static void setPrefFontSize(String s){
        mPrefFontSize = s;
    }

    private static String mPrefFontColor = "";
    public static String getPrefFontColor(){
        return mPrefFontColor;
    }
    public static void setPrefFontColor(String s){
        mPrefFontColor = s;
    }

    private static String mPrefBGColor = "";
    public static String getPrefBGColor(){
        return mPrefBGColor;
    }
    public static void setPretBGColor(String s){
        mPrefBGColor = s;
    }

    private static Boolean mPrefSmartSave = false;
    public static Boolean getPrefSmartSave(){
        return mPrefSmartSave;
    }
    public static void setPretSmartSave(Boolean s){
        mPrefSmartSave = s;
    }

    private static Integer mPrefDefaultTab = 0;
    public static Integer getPrefDefaultTab(){
        return mPrefDefaultTab;
    }
    public static void setPretDefaultTab(Integer s){
        mPrefDefaultTab = s;
    }

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

}
