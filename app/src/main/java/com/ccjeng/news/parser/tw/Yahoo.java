package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/18.
 */
public class Yahoo implements INewsParser {
    private static final String TAG = "Yahoo";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        //replace url "news" to "mobi"

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";//doc.select("div#image").first().text();
        String body = "";
        try {
            title = doc.select("h1").text();
            time = "";
            body = doc.select("div#main-2-Story").html();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + body);

        String b = cleaner(body);
        Log.d(TAG, "html=" + b);

        return Webpage.htmlDrawer(title, time, b);

    }

    private String cleaner(String rs) {

        //rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");

        Whitelist wlist=new Whitelist();

        wlist.addTags("p");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img","src");

        return Jsoup.clean(rs,wlist);

        //return rs;

    }

}
