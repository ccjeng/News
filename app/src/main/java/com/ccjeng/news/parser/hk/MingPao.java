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
public class MingPao implements INewsParser {
    private static final String TAG = "MingPao";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";
        try {
            title = doc.select("div#maincontent > div.group > div.span_8_of_12 > div.incontent > hgroup").text();
            time = doc.select("div#maincontent > div.group > div.span_8_of_12 > div#blockcontent > div#articleTop > div.date").text();
            body = doc.select("div#maincontent > div.group > div.span_8_of_12 > div#blockcontent> span#advenueINTEXT > article").html();
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

        rs = rs.replace("掌握最新消息，請Like「","<!--");

        Whitelist wlist = new Whitelist();

        wlist.addTags("txt","p");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img", "src");

        return Jsoup.clean(rs, wlist);

    }
}
