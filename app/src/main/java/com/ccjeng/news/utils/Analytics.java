package com.ccjeng.news.utils;

import android.app.Activity;

import com.ccjeng.news.view.base.BaseApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * Created by andycheng on 2015/6/28.
 */
public class Analytics {


    public static void trackerPage(Activity activity) {
        Tracker t = ((BaseApplication) activity.getApplication()).getTracker(
                BaseApplication.TrackerName.APP_TRACKER);
        t.setScreenName(activity.getClass().getSimpleName());
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public static void trackEvent(Activity activity
            , String category, String action, String label, long value) {
        Tracker t = ((BaseApplication) activity.getApplication()).getTracker(
                BaseApplication.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }


}
