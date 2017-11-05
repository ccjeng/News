package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;
import com.ccjeng.news.view.base.BaseApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/23.
 */
public class ETToday extends AbstractNews {
    private static final String TAG = "ETToday";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {

            if (link.contains("/star/") || link.contains("/travel/")) {
                title = doc.select("h2.title").text();
                Log.d(TAG, "title2=" + title);
            } else {
                title = doc.select("h1.title").text();
            }

            time = doc.select("span.news-time").text();
            body = doc.select("div.story").html();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BaseApplication.APPDEBUG) {
            Log.d(TAG, "title = " + title);
            Log.d(TAG, "time = " + time);
            Log.d(TAG, "body = " + body);
        }
        String b = cleaner(body);

        if (BaseApplication.APPDEBUG) {
            Log.d(TAG, "html=" + b);
        }
        return Webpage.htmlDrawer(title, time, b);

    }

    @Override
    public Boolean isEmptyContent() {
        if (body.trim().equals(""))
            return true;
        else
            return false;
    }

    protected String cleaner(String rs) {

        rs = rs.replace("<img src=\"//","<img src=\"https://");
        rs = rs.replace("<div class=\"test-keyword\"> ", "<!--");
        rs = rs.replace("\"//static.ettoday.net","\"http://static.ettoday.net");


        Whitelist wlist = new Whitelist();

        wlist.addTags("p", "br");
        wlist.addTags("table", "tbody", "tr", "td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
