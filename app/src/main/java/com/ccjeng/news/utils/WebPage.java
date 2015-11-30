package com.ccjeng.news.utils;

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

}
