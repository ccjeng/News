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
 * Created by andycheng on 2015/11/24.
 */
public class HKHeadline extends AbstractNews {
    private static final String TAG = "HKHeadline";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";

        try {
            if (link.contains("ent/")) {
                title = doc.select("td.bodytext > b").text();
                time = doc.select("td.bodytext > font").text();
                body = doc.select("div.hkadj").html();  //todo + "<p>" + doc.select("img.imgtop").html();

            } else {
                title = doc.select("div.headlinetitle").text();
                time = doc.select("table span.newsheadlinetime").text();
                body = doc.select("div.bodytext_v1").html() + "<p>" + doc.select("table tr td.news_line02").html();

            }


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

        rs = rs.replace("www.hkheadline.com","");
        rs = rs.replace("&nbsp;","");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","br");
       // wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
