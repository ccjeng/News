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
public class LibertyTimes implements INewsParser {
    private static final String TAG = "LibertyTimes";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {

            if (link.contains("sports")) {
                title = doc.select("div.Btitle").text();
                time = doc.select("div.date").get(0).text();
                body = doc.select("div.news_content").html();
            } else {
                title = doc.select("h1").text();
                time = doc.select("div#newstext span").get(0).text();
                body = doc.select("div#newstext").html();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "rs = " + body);
        Log.d(TAG, "body = " + cleaner(body));

        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String rs) {

        rs = rs.replace(" 廣告","");
        rs = rs.replace("data-original=","src=");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
