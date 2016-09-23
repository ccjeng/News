package com.ccjeng.news.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ccjeng.news.view.NewsView;
import com.ccjeng.news.view.base.BaseApplication;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Webpage {
    private static final String TAG = "Webpage";

    public static String htmlDrawer(String title, String time, String body) {

        String htmlTagStart = "<body bgcolor=" + BaseApplication.getPrefBGColor()
                + "><font color="+ BaseApplication.getPrefFontColor()+">";

        String htmlTagEnd = "</font></body>";

        String fontSize = "<font size=+"+ BaseApplication.getPrefFontSize() +">";

        String html = htmlTagStart + "<h2>" + title + "</h2>"
                + "<div>" + time + "</div>";

        html = html + "<hr>";


        if (BaseApplication.getPrefSmartSave()) {
            Log.d(TAG, "enable smart save");
            String smartSaveTag = "<small><small>[這是圖片，已啟用智能節費，停止圖片下載]</small></small></br></br>";
            body = body.replace("<img src", smartSaveTag + "<imgsrc");
        }
        html = html + fontSize + body + "</font>" + htmlTagEnd;

        return html;
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static double getWidth(NewsView context) {
        int width = 0;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Configuration config = context.getResources().getConfiguration();
        //int vWidth = 0;
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            width = dm.heightPixels;
        else
            width = dm.widthPixels;

        //int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        //Log.d(TAG, "currentapiVersion = " + currentapiVersion);
        //Log.d(TAG, "width px = " + width);
        //Log.d(TAG, "width dip = " + Webpage.px2dip(context, width));

        return Webpage.px2dip(context, width);

    }
}
