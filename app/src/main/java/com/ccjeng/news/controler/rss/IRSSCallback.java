package com.ccjeng.news.controler.rss;

import com.ccjeng.news.controler.rss.RSSFeed;

/**
 * Created by andycheng on 2016/3/21.
 */
public interface IRSSCallback {

    public void onRSSReceived(RSSFeed rssFeed);

}
