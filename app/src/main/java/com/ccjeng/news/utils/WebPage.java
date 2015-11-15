package com.ccjeng.news.utils;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Webpage {

    public static String htmlDrawer(String title, String time, String body) {
        String html = "<h2>" + title + "</h2>"
                + "<div>" + time + "</div>";

        html = html + "<hr><br/>";
        html = html + "<div>" + body + "</div>";

        return html;
    }
}
