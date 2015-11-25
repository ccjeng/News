package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/25.
 */
public class Sun implements INewsParser {
    private static final String TAG = "Sun";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            title = doc.select("h1").text();
            time = "";//doc.select("div.createddate").text();
            body = doc.select("div.newsText > div.first_content").html()
                    + doc.select("div.newsText > div.other_content").html()
                    + doc.select("div.para_photo").html();
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

        rs = rs.replace("src=\"/cnt/","src=\"http://the-sun.on.cc/cnt/");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p");
        //wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
