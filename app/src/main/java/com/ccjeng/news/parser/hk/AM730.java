package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;
import com.ccjeng.news.view.base.BaseApplication;

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
            title = doc.select("div.news-detail-title").first().text();
            time = doc.select("div.news-detail-date").text();
            body = doc.select("div.news-detail-content").html();

                    //doc.select("ul.slides").get(0).html();

            // > li.flex-active-slide

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

        rs = rs.replace("<img src=\"https://d2e7nuz2r6mjca.cloudfront.net/assets/img/logo_bottom.png\" class=\"logo\">","");
       // rs = rs.replace("<img src=\"uploads","<img src=\"http://www.am730.com.hk/uploads");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","br","strong");
        //wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
