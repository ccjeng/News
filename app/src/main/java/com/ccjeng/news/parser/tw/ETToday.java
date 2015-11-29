package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;

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
            if (link.contains("/star/")) { //影劇
                title = doc.select("div.module_1 > h2.title").text();
                time = doc.select("div.module_1 > div.story > p.date").text();
                body = doc.select("div.module_1 > div.story").html();

            } else if (link.contains("/sport/")) { //運動
                title = doc.select("div.subjcet_article > h1.title").text();
                time = doc.select("div.subjcet_article > div.date").text();
                body = doc.select("div.subjcet_article > div.story").html();

            } else if (link.contains("/travel/")) { //旅遊
                title = doc.select("div.subjcet_news > h2.title").text();
                time = doc.select("div.subjcet_news > div.date").text();
                body = doc.select("div.subjcet_news > div.story").html();
            } else {
                title = doc.select("div.subjcet_news header h2").text();
                time = doc.select("span.news-time").text();
                body = doc.select("div.story > sectione").html();
            }
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

    @Override
    public Boolean isEmptyContent() {
        if (body.trim().equals(""))
            return true;
        else
            return false;
    }

    protected String cleaner(String rs) {

        rs = rs.replace("<div class=\"test-keyword\"> ", "<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","br");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
