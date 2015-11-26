package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/24.
 */
public class HKHeadline implements INewsParser {
    private static final String TAG = "HKHeadline";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            if (link.contains("ent")) {
                title = doc.select("td.bodytext > b").text();
                time = doc.select("td.bodytext > font").text();
                body = doc.select("div.hkadj").html(); //todo + "<p>" + doc.select("img.imgtop").html();

            } else {
                title = doc.select("div.headlinetitle").text();
                time = doc.select("span.newsheadlinetime").text();
                body = doc.select("div.bodytext_v1").html() + "<p>" + doc.select("td.news_line02").html();
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

    private String cleaner(String rs) {

        rs = rs.replace("www.hkheadline.com","");

        Whitelist wlist = new Whitelist();

        wlist.addTags("p","br");
       // wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }

}
