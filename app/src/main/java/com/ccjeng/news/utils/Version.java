package com.ccjeng.news.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by andycheng on 2015/11/30.
 */
public class Version {
    public static final String TAG = "Version";
    private static String mLastVersionRun;
    private static SharedPreferences prefs = null;

    public static void setLastVersionRun(String lastVersionRun) {
        put("LastVersionRun", lastVersionRun);
        Version.mLastVersionRun = lastVersionRun;
    }

    public static String getLastVersionRun() {
        return mLastVersionRun;
    }

    public static boolean newVersionInstalled(Activity a) {
        if (prefs==null)
            prefs = PreferenceManager.getDefaultSharedPreferences(a);
        String thisVersion = getVersion(a);
        Version.mLastVersionRun = prefs.getString("LastVersionRun", "");
        String lastVersionRun = mLastVersionRun;

        setLastVersionRun(thisVersion);
        if (thisVersion.equals(lastVersionRun)) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isNewInstallation(Activity a) {
        if (prefs==null)
            prefs = PreferenceManager.getDefaultSharedPreferences(a);
        if (prefs.getString("LastVersionRun", "").equals("")) {
            setLastVersionRun(getVersion(a));
            return true;
        } else
            return false;
    }
    /**
     * Retrieves the packaged version of the application
     *
     * @param a
     *            - The Activity to retrieve the current version
     * @return the version-string
     */
    public static String getVersion(Activity a) {
        String result = "";
        try {
            PackageManager manager = a.getPackageManager();
            PackageInfo info = manager.getPackageInfo(a.getPackageName(), 0);
            result = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "Unable to get application version: " + e.getMessage());
            result = "";
        }
        return result;
    }

    private static void put(String constant, Object o) {
        SharedPreferences.Editor editor = prefs.edit();
        if (o instanceof String) {
            editor.putString(constant, (String) o);
        } else if (o instanceof Integer) {
            editor.putInt(constant, (Integer) o);
        } else if (o instanceof Long) {
            editor.putLong(constant, (Long) o);
        } else if (o instanceof Boolean) {
            editor.putBoolean(constant, (Boolean) o);
        }
        editor.commit();
    }


}
