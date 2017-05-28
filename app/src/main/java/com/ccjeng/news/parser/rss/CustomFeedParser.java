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
        final String TAG = "CustomFeedParser";
        RSSFeed feed = new RSSFeed();
        RSSItem item;
        Document doc = Jsoup.parse(content);
        Elements links;
        Element link;
        String root;

        try {

            if (url.contains("hkm.appledaily.com")) {
                root = "http://hkm.appledaily.com/";

                if(url.contains("category=magazine")) { //壹週
                    links = doc.select("div.slider");
                    for (Element k : links) {
                        item = new RSSItem();
                        item.setTitle(k.select("p.caption").text());
                        item.setLink(root + k.select("a").attr("href"));
                        item.setDescription("");
                        item.setImg(k.select("div.background").attr("style").replace("background-image:url('","").replace("')",""));
                        feed.addItem(item);
                    }
                    links = doc.select("div.item");
                    for (Element k : links) {
                        item = new RSSItem();
                        item.setTitle(k.select("p").get(1).text());
                        item.setLink(root + k.select("a").attr("href"));
                        item.setDescription("");
                        item.setImg(k.select("img").first().absUrl("src"));
                        feed.addItem(item);
                    }

                } else {
                    links = doc.select("ul li a");
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
                links = doc.select("div.item");

                for (Element k : links) {
                    item = new RSSItem();
                    //Log.d("daliulian", k.select("div.media > div.title").text());
                    item.setTitle(k.select("div.media > div.title").text());
                    item.setLink(k.select("div.media > div.title a").attr("href"));
                    item.setPubDate("");
                    item.setDescription(k.select("div.cover").html().replace("/img","http://www.thegreatdaily.com/img"));
                    feed.addItem(item);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return feed;
    }
}
