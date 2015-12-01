package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.News;
import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/25.
 */
public class AM730 extends AbstractNews {
    private static final String TAG = "AM730";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {
            //// TODO: 2015/12/1 title & time
            title = doc.select("div#article > h2").text();
            time = doc.select("div#article > div#date").text();
            body = doc.select("div#article_content").html() + doc.select("ul.slides").html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (News.APPDEBUG) {
            Log.d(TAG, "title = " + title);
            Log.d(TAG, "time = " + time);
            Log.d(TAG, "body = " + body);
        }

        String b = cleaner(body);
        if (News.APPDEBUG) {
            Log.d(TAG, "html=" + b);
        }
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

        rs = rs.replace("<img src=\"images/am730_article_logo.jpg\">","");
        rs = rs.replace("<img src=\"uploads","<img src=\"http://www.am730.com.hk/uploads");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p");
        //wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
