package com.ccjeng.news.controler.rss;

/**
 * Created by andycheng on 2016/3/21.
 */
public interface IRSSCallback {

    public void onRSSReceived(RSSFeed rssFeed);
    public void onRSSFailed(String error);


}
