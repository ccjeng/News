package com.ccjeng.news.parser.rss;

import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by andycheng on 2016/4/9.
 */
public class CustomFeedParser {

    public RSSFeed getFeeds(String url, String content) {
        RSSFeed feed = new RSSFeed();
        RSSItem item;
        Document doc = Jsoup.parse(content);
        Elements links;
        String root;

        try {

            if (url.contains("hkm.appledaily.com")) {
                links = doc.select("ul li a");
                root = "http://hkm.appledaily.com/";

                for (Element k : links) {
                    if (k.attr("href").contains("detail.php")) {

                        String u = k.attr("href");

                        item = new RSSItem();
                        item.setTitle(k.select("p").text());
                        item.setLink(root + u);

                        if (url.contains("=daily")) {
                            //日報
                            item.setPubDate(u.substring(u.length() - 8));
                        } else if (url.contains("=instant")) {
                            //即時新聞
                            item.setPubDate(k.select("label").text());
                        }
                        item.setDescription("");
                        item.setImg(k.select("img").first().absUrl("src"));
                        feed.addItem(item);
                    }
                }

            } else if (url.contains("am730.com.hk")) {
                links = doc.select("div#news_index_box");
                root = "http://www.am730.com.hk/";

                for (Element k : links) {
                    item = new RSSItem();
                    item.setTitle(k.select("div#news_index_title").text());
                    item.setLink(root + k.select("div#news_index_title a").attr("href"));
                    item.setPubDate(k.select("div#news_index_date").text());
                    item.setDescription(k.select("div#news_index_desc").text());
                    feed.addItem(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return feed;
    }
}
