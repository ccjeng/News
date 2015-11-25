package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/19.
 */
public class UDN implements INewsParser {
    private static final String TAG = "Yahoo";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            title = doc.select("h2#story_art_title").text();
            time = doc.select("div#story_bady_info h3").text();
            body = doc.select("div#story_body_content").html();
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

        rs = rs.replace("<div class=\"social_bar\"> ","<!--");
        rs = rs.replace("<div id=\"set_font_size\" class=\"only_web\">","<!--");
        rs = rs.replace("<a href=\"####\" class=\"photo_pop_icon\">","<!--");
        rs = rs.replace("<div class=\"photo_pop\">","<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","h4");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");


        return Jsoup.clean(rs, wlist);


    }

}
