package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/20.
 */
public class YamNews extends AbstractNews {
    private static final String TAG = "YamNews";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            title = doc.select("li.title > h2").text();
            time = doc.select("li.info > time").text() + "<br/>" + doc.select("li.info > span").text();
            body = doc.select("li.photo").html() +  doc.select("li.photoData > h3").text() + doc.select("div#news_content").html();
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

    protected String cleaner(String rs) {

        rs = rs.replace("<!-- 蕃plus + AD START -->", "<!-- 蕃plus + AD START ");
        rs = rs.replace("<!-- Customized -->", "<!-- Customized ");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","h3");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
