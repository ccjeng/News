package com.ccjeng.news.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSService;
import com.ccjeng.news.presenter.base.BasePresenter;
import com.ccjeng.news.utils.Category;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andycheng on 2016/9/22.
 */

public class NewsRSSListPresenter extends BasePresenter<NewsRSSListView>
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = NewsRSSListPresenter.class.getSimpleName();

    private NewsRSSListView view;
    private Context context;
    private CompositeSubscription subscriptions;

    public NewsRSSListPresenter(NewsRSSListView view, Context context) {
        this.view = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /* SwipeRefreshLayout
    * * */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //refresh data
                view.refreshData();
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }

    public void getRSSData(String tabName, int sourceNumber, int itemNumber) {

        Category cat = new Category(context);
        String[] feedURLArray = cat.getFeedURL(tabName, sourceNumber);
        String rssFeedURL = null;

        //get RSS Feed
        if (feedURLArray != null) {
            rssFeedURL = feedURLArray[itemNumber];

            final RSSService rssService = new RSSService(rssFeedURL);
            subscriptions.add(
                    rssService.request()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.toString());
                                    view.showError(R.string.network_error);
                                }

                                @Override
                                public void onNext(String s) {
                                    RSSFeed rssFeed = rssService.parse(s);
                                    if (rssFeed != null) {
                                        view.setListView(rssFeed);
                                    } else {
                                        view.showError(R.string.data_error);
                                    }
                                }
                            })
            );

        }

    }
}
