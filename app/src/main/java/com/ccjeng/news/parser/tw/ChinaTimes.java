package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/20.
 */
public class ChinaTimes implements INewsParser {
    private static final String TAG = "ChinaTimes";

    @Override
    public String parseHtml(String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            title = doc.select("header > h1").text();
            time = doc.select("div.reporter > time").text() + "<br/>" + doc.select("div.rp_name").text();
            body = doc.select("article.clear-fix").get(1).html();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + cleaner(body));

        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String rs) {

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","figcaption");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);


    }


}
