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

        String fontSize = "<font size="+ News.getPrefFontSize() +">";

        String html = htmlTagStart + "<h2>" + title + "</h2>"
                + "<div>" + time + "</div>";

        html = html + "<hr>";

        html = html + fontSize + "<div><big>" + body + "</big></div></font>" + htmlTagEnd;

        return html;
    }

}
