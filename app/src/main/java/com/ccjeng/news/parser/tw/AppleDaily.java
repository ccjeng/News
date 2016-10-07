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
 * Created by andycheng on 2015/11/15.
 */
public class AppleDaily extends AbstractNews {

    private static final String TAG = "AppleDaily";
    private String body = "";

    @Override
    public String parseHtml(final String link, String content) throws IOException {
        //Log.d(TAG, "Response = " + content);

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        Log.d(TAG, content);

        try {
            title = doc.select("h1#h1").text();
            time = doc.select("time").first().text();
            body = doc.select("div.articulum").html();

        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, e.getMessage());
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
        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    @Override
    public Boolean isEmptyContent() {
        if (body.trim().equals(""))
            return true;
        else
            return false;
    }

    protected String cleaner(String rs) {

        rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");
        rs = rs.replace("/thumbnail/","/IphoneThumbnail/");
        rs = rs.replace("_160x160.jpg","_280x.jpg");

        rs = rs.replace("<b>","<!--");
        rs = rs.replace("</b>","-->");

        rs = rs.replace("<h2 id=\"bhead\">","<p><b>");
        rs = rs.replace("</h2>","</b></p>");

        Whitelist wlist=new Whitelist();

        wlist.addTags("p","b","span","br");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img","src");

        return Jsoup.clean(rs,wlist);


    }
}
