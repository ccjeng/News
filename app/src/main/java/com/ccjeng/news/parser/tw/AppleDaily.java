package com.ccjeng.news.parser.tw;

import android.util.Log;

import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Webpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/15.
 */
public class AppleDaily implements INewsParser {

    private static final String TAG = "AppleDaily";

    @Override
    public String parseHtml(final String link, String content) throws IOException {
        //Log.d(TAG, "Response = " + content);

        Document doc = Jsoup.parse(content);

        String title = "";
        String time = "";
        String body = "";

        try {
            title = doc.select("h1#h1").text();
            time = doc.select("time").first().text();
            body = doc.select("div.articulum").html();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "title = " + title);
        Log.d(TAG, "time = " + time);
        Log.d(TAG, "body = " + cleaner(body));

        return Webpage.htmlDrawer(title, time, cleaner(body));

    }

    private String cleaner(String rs) {

        rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");

        Whitelist wlist=new Whitelist();

        wlist.addTags("p","h2","hr","span","br");
        wlist.addTags("table","tbody","tr","td");
        wlist.addTags("img").addAttributes("img","src");

        return Jsoup.clean(rs,wlist);


        /*
        rs = rs.replace("<a ", "<a");
        rs = rs.replace(" href", "href");
        //rs = rs.replace(" class", "class");
        //rs = rs.replace(" id", "id");
        rs = rs.replace("<script ", "<script");
        rs = rs.replace("<img src=\"http://twimg.edgesuite.net/appledaily/images/twitterline.png\">", "");
        rs = rs.replace("<iframe", "<!--");
        rs = rs.replace("</iframe>", "-->");
        rs = rs.replace("<script", "<!--");
        rs = rs.replace("</script>", "-->");
        rs = rs.replace(">更多文章","><!--");
        rs = rs.replace("figure class","figureclass");
        rs = rs.replace("<ahref=\"/realtimenews", "<!--");
*/

        //return clean;
    }
}
