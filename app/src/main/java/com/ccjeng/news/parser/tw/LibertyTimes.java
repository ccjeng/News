package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/20.
 */
public class LibertyTimes extends AbstractNews {
    private static final String TAG = "LibertyTimes";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {

            if (link.contains("sports")) {
                title = doc.select("div.Btitle").text();
                time = doc.select("div.date").get(0).text();
                body = doc.select("div.news_content").html();

            } else if (link.contains("entertainment")) {
                title = doc.select("div.news_content > h1").text();
                time = doc.select("div.news_content > div.date").text();
                body = doc.select("div.news_content").html();

            } else if (link.contains("opinion")) {
                title = doc.select("div.conbox > h2").text();
                time = doc.select("div.conbox > div.writer > span").text();
                body = doc.select("div.conbox > div.cont").html();

            }
            else {
                title = doc.select("h1").text();
                time = doc.select("div#newstext span").get(0).text();
                body = doc.select("div#newstext").html();
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

        rs = rs.replace(" 廣告","");
        rs = rs.replace("data-original=","src=");
        //rs = rs.replace("<span>","<p>");
        //rs = rs.replace("</span>","</p>");
        rs = rs.replace("相關新聞", "<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p", "span");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
