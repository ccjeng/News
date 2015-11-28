package com.ccjeng.news.parser;

import android.util.Log;

import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/19.
 */
public class Standard extends AbstractNews {

    private static final String TAG = "Standard";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        Document doc = Jsoup.parse(content);

        String title ="";
        String time = "";
        String body = content;

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + body);

        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    protected String cleaner(String rs) {

        Whitelist wlist = null;
        try {
            wlist = Whitelist.basicWithImages();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Jsoup.clean(rs,wlist);

    }

}
