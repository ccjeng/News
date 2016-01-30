package com.ccjeng.news.utils;

import android.content.Context;
import android.util.Log;

import com.ccjeng.news.News;
import com.ccjeng.news.parser.Standard;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Webpage {
    private static final String TAG = "Webpage";

    public static String htmlDrawer(String title, String time, String body) {

        String htmlTagStart = "<body bgcolor=" + News.getPrefBGColor()
                + "><font color="+ News.getPrefFontColor()+">";

        String htmlTagEnd = "</font></body>";

        String fontSize = "<font size=+"+ News.getPrefFontSize() +">";

        String html = htmlTagStart + "<h2>" + title + "</h2>"
                + "<div>" + time + "</div>";

        html = html + "<hr>";


        if (News.getPrefSmartSave()) {
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


}
