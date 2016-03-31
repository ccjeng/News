package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.view.base.News;
import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/24.
 */
public class OrientalDaily extends AbstractNews {
    private static final String TAG = "OrientalDaily";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {
            title = doc.select("div#leadin > h1").text();
            time = "";
            body = doc.select("div#leadin > div.leadin").html() + doc.select("div#contentCTN > div#contentCTN-right").html();
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

        rs = rs.replace("src=\"/cnt", "src=\"http://orientaldaily.on.cc/cnt");
        rs = rs.replace("<h3>","<p><b>");
        rs = rs.replace("</h3>","</b></p>");
        rs = rs.replace("<!--AD-->","<!--");
        rs = rs.replace("<div id=\"articleNav\">","<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","b");
        //wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
