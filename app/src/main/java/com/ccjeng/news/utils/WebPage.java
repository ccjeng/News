package com.ccjeng.news.utils;

/**
 * Created by andycheng on 2015/11/15.
 */
public class Webpage {

    public static String htmlDrawer(String title, String time, String body) {
        String html = "<h2>" + title + "</h2>"
                + "<div>" + time + "</div>";

        html = html + "<hr><br/>";

        if (body.trim().equals("")) {
          //get default parser

        } else {
            html = html + "<div><big>" + body + "</big></div>";
        }
        return html;
    }
}
