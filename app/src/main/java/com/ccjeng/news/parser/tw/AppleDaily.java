package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/15.
 */
public class AppleDaily implements INewsParser {

    private static final String TAG = "AppleDaily";

    @Override
    public String parseHtml(String content) throws IOException {
        Log.d(TAG, "Response = " + content);

        Document doc = Jsoup.parse(content);

        String title = doc.select("h1#h1").text();
        String time = doc.select("time").first().text();
        String body = doc.select("div.articulum").html();

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + body);

        //draw webview
        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String body) {


        return body;
    }
}
