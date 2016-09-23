package com.ccjeng.news.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.web.NewsHandler;
import com.ccjeng.news.presenter.base.BasePresenter;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.view.base.BaseApplication;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andycheng on 2016/9/22.
 */

public class NewsViewPresenter extends BasePresenter<NewsViewView> {

    private static final String TAG = NewsViewPresenter.class.getSimpleName();
    private NewsViewView view;
    private Context context;
    private CompositeSubscription subscriptions;

    public NewsViewPresenter(NewsViewView view, Context context) {
        this.view = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    public void getNews(String tabName, int sourceNumber, String url) {
        String charset = Category.getEncoding(tabName, sourceNumber);//"utf-8";
        String newsUrl = Network.checkNewsViewURL(url);

        if (BaseApplication.APPDEBUG) {
            Log.d(TAG, "charset = " + charset);
            Log.d(TAG, "url = " + newsUrl);
        }

        NewsHandler newsHandler = new NewsHandler(newsUrl);

        subscriptions.add(
                newsHandler.getNewsContent(charset)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.showError(R.string.data_error);
                                Log.e(TAG, "error = " + e.getMessage());
                            }

                            @Override
                            public void onNext(String html) {
                                view.drawHtmlPage(html);
                            }
                        })
        );

    }

    public void menuOpenBrowser(String newsUrl) {
        Uri uri = Uri.parse(newsUrl);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void menuShare(String message) {
        //Todo Shorten URL
        // https://www.learn2crack.com/2014/01/android-using-goo-gl-url-shortener-api.html
        // http://jkoder.com/url-shortening-using-google-api/

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.sharing_description)));

    }

    @Override
    public void onDestroy() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }
}
