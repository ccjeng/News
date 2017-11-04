package com.ccjeng.news.parser.rss;

import android.util.Log;

import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSItem;
import com.ccjeng.news.utils.Network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by andycheng on 2016/4/9.
 */
public class CustomFeedParser {

    public RSSFeed getFeeds(String url, String content) {
        final String TAG = "CustomFeedParser";
        RSSFeed feed = new RSSFeed();
        RSSItem item;
        Document doc = Jsoup.parse(content);
        Elements links;
        Element link;
        String root;

        try {

            if (url.contains("appledaily") && url.contains("hk")) {

                links = doc.select("div.content-list > ul.list > li");

                for (Element k : links) {
                    item = new RSSItem();
                    item.setTitle(k.select("a > p").text().replace("　　",""));
                    item.setLink(k.select("a").attr("href"));
                    item.setDescription("");
                    item.setImg(k.select("a > img").attr("src"));

                    Log.d("appledaily", k.select("a > p").text());

                    if (k.select("a > p").hasText()) {
                        feed.addItem(item);
                    }
                }

            } else if (url.contains("am730.com.hk")) {
                links = doc.select("div.columns-left div.animationBox");
                //root = "http://www.am730.com.hk/";

                String postUrl = "";
                for (Element k : links) {
                    postUrl = k.select("div.newsTitle a.aGray").attr("href");
                    //Log.d("AM730", k.select("div.newsTitle").text() + postUrl);

                    item = new RSSItem();
                    item.setTitle(k.select("div.newsTitle").text());
                    item.setLink(postUrl);
                    item.setPubDate(k.select("div.newsUpdateTime").text());
                    item.setDescription("");
                    if (!postUrl.equals("")) {
                        feed.addItem(item);
                    }
                }
            } else if (url.contains("thegreatdaily")) {
                links = doc.select("div.snippet");

                for (Element k : links) {
                    item = new RSSItem();
                    //Log.d("daliulian", k.select("div.media > div.title").text());
                    item.setTitle(k.select("div.media h3.title").text());
                    item.setLink(k.select("div.media > div.media-body a").attr("href"));
                    item.setPubDate("");
                    item.setDescription(k.select("div.media-left").html().replace("/img","http://www.thegreatdaily.com/img"));
                    feed.addItem(item);
                }

            } else if (url.contains("appledaily") && url.contains("tw")) {

                root = "";
                if (url.contains("daily")) {
                    root = "https://" + Network.getDomainName(url);
                }

                links = doc.select("article.nm-content li.art");

                for (Element k : links) {
                    item = new RSSItem();
                    item.setTitle(k.select("p.art-title").text());
                    item.setLink(root + k.select("a").attr("href"));
                    item.setDescription("");
                    item.setPubDate(k.select("p.art-stats span.time").text());
                    item.setImg("https:" + k.select("img").attr("src"));

                    //Log.d("appledaily", "main-articles = " + root + k.select("a").attr("href"));

                    feed.addItem(item);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return feed;
    }
}
