package com.ccjeng.news.presenter;

import com.ccjeng.news.controler.rss.RSSFeed;

/**
 * Created by andycheng on 2016/9/22.
 */

public interface NewsRSSListView  {

    void refreshData();
    void showError(int message);
    void setListView(RSSFeed rssFeed);

}
