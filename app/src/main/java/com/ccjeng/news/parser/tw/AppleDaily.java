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
        Log.d(TAG, "body = " + cleaner(body));


        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String rs) {

        rs = rs.replace("<a ", "<a");
        rs = rs.replace(" href", "href");
        //rs = rs.replace(" class", "class");
        //rs = rs.replace(" id", "id");
        rs = rs.replace("<script ", "<script");
        rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");
        rs = rs.replace("<iframe", "<!--");
        rs = rs.replace("</iframe>", "-->");
        rs = rs.replace("<script", "<!--");
        rs = rs.replace("</script>", "-->");
        rs = rs.replace(">更多文章","><!--");

        return rs;
    }
}
