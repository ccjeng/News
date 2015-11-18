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
    public String parseHtml(String content) throws IOException {
        Log.d(TAG, "Response = " + content);


        //replace url "news" to "mobi"

        Document doc = Jsoup.parse(content);

        String title = doc.select("h1").text();
        String time = "";//doc.select("div#image").first().text();
        String body = doc.select("div#main-2-Story").html();

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + cleaner(body));

        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String rs) {

        //rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");

        Whitelist wlist=new Whitelist();

        wlist.addTags("p");
        wlist.addTags("img").addAttributes("img","src");

        return Jsoup.clean(rs,wlist);

        //return rs;

    }

}
