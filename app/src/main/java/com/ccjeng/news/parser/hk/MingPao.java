package com.ccjeng.news.parser.hk;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by andycheng on 2015/11/24.
 */
public class MingPao implements INewsParser {
    private static final String TAG = "MingPao";

    @Override
    public String parseHtml(final String link, String content) throws IOException {

        StringBuilder sb = new StringBuilder();

        String title = "";
        String time = "";
        String body = "";
        try {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            boolean beginFind = false;
            String s;

            while (null != (s = br.readLine())) {
                if (s.trim().contains("<hgroup>")) {
                    beginFind = true;
                } else if (s.trim().contains("</artcle>")) {
                    break;
                }
                if (beginFind) {
                    sb.append(s.trim());
                }
            }

            body = sb.toString();

            br.close();

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
