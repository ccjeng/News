package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;
import com.ccjeng.news.view.base.BaseApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/24.
 */
public class CNA extends AbstractNews {
    private static final String TAG = "CNA";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {
            title = doc.select("div.news_title > h1").text();
            time = doc.select("div.update_times > p.blue").text();
            body = doc.select("div.news_article > div.article_box").html();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BaseApplication.APPDEBUG) {
            Log.d(TAG, "title = " + title);
            Log.d(TAG, "time = " + time);
            Log.d(TAG, "body = " + body);
        }
        String b = cleaner(body);
        if (BaseApplication.APPDEBUG) {
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

        rs = rs.replace("<br>", "<p>");
        rs = rs.replace("※你可能還想看：","<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
