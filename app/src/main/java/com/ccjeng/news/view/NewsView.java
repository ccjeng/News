package com.ccjeng.news.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.web.IWebCallback;
import com.ccjeng.news.controler.web.NewsHandler;
import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.utils.PreferenceSetting;
import com.ccjeng.news.utils.UI;
import com.ccjeng.news.utils.Webpage;
import com.ccjeng.news.view.base.BaseActivity;
import com.ccjeng.news.view.base.News;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mopub.mobileads.MoPubView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

//todo 加上新聞小幫手檢核 sample: https://github.com/g0v/newshelper-extension/blob/master/background.js

public class NewsView extends BaseActivity {

    private static final String TAG = NewsView.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    public WebView webView;
    @Bind(R.id.progress_wheel)
    public ProgressWheel progressWheel;
    @Bind(R.id.main)
    public NestedScrollView main;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;

    private Analytics ga;

    private String newsName;
    private String newsUrl;
    private String newsTitle;
    private MoPubView moPubView;
    private int sourceNumber;
    private String tabName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        ga = new Analytics();
        ga.trackerPage(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        PreferenceSetting.getPreference(this);

        Bundle bundle = this.getIntent().getExtras();
        sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
        tabName = bundle.getString("SourceTab");
        newsName = bundle.getString("NewsName");

        newsUrl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        main.setBackgroundColor(Color.parseColor(News.getPrefBGColor()));

        moPubView = (MoPubView) findViewById(R.id.adview);

        if (Network.isNetworkConnected(this)) {

            progressWheel.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);

            IWebCallback callback = new IWebCallback() {
                @Override
                public void onWebContentReceived(String html) {
                    drawHtmlPage(html);
                }

                @Override
                public void onWebContentError(String error) {
                    UI.showErrorSnackBar(coordinator, R.string.data_error);
                    Log.e(TAG, "error = " + error);
                }
            };

            String charset = Category.getEncoding(tabName, sourceNumber);//"utf-8";

            if (News.APPDEBUG) {
                Log.d(TAG, "charset = " + charset);
                Log.d(TAG, "url = " + newsUrl);
            }

            newsUrl = Network.checkNewsViewURL(newsUrl);

            NewsHandler newsHandler = new NewsHandler(this, callback, newsUrl);
            newsHandler.getNewsContent(charset);

            Network.AdView(moPubView, Constant.AD_MoPub_View);

        } else {
            UI.showErrorSnackBar(coordinator, R.string.network_error);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        PreferenceSetting.getPreference(this);
    }


    @Override
    protected void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }

        super.onDestroy();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);

        MenuItem menuItem1 = menu.findItem(R.id.action_browser);
        menuItem1.setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_web).actionBar().color(Color.WHITE));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_browser:
                menuOpenBrowser();
                break;
            case R.id.action_share:
                menuShare();
                break;
            case R.id.action_setting:
                startActivity(new Intent(NewsView.this, Preference.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void menuOpenBrowser() {

        Uri uri = Uri.parse(newsUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

    private void menuShare() {
        //Todo Shorten URL
        // https://www.learn2crack.com/2014/01/android-using-goo-gl-url-shortener-api.html
        // http://jkoder.com/url-shortening-using-google-api/

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "["+ newsName + "] " + newsTitle + " " + newsUrl);
        startActivity(Intent.createChooser(intent, getString(R.string.sharing_description)));

    }

    private void drawHtmlPage(String html) {

        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            //context.webView.getSettings().setSupportZoom(true);
            //context.webView.getSettings().setBuiltInZoomControls(true);
            //context.webView.getSettings().setCacheMode(2); //LOAD_NO_CACHE
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            //context.webView.getSettings().setLoadWithOverviewMode(true);
            //context.webView.getSettings().setUseWideViewPort(true);
        }

        Category cat = new Category(NewsView.this);
        AbstractNews parser = cat.getNewsParser(tabName, sourceNumber);

        try {

            String newsContent = parser.parseHtml(newsUrl, html);

            progressWheel.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);

            if (parser.isEmptyContent()) {
                //if parse result is empty, then show webview directly..
                Analytics ga = new Analytics();
                ga.trackEvent(NewsView.this, "Error", "Empty Content", newsUrl, 0);

                UI.showErrorSnackBar(coordinator, R.string.parsing_error_transfer);

                webView.loadUrl(newsUrl);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        progressWheel.setVisibility(View.VISIBLE);
                        main.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progressWheel.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                    }
                });
                webView.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        progressWheel.setProgress((float) newProgress / 100);

                        if (newProgress > 90) {
                            progressWheel.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                        }
                    }
                });

            } else {

                webView.loadDataWithBaseURL(null, newsContent, "text/html", "utf-8", "about:blank");

                //image fit screen
                final String js;
                js= "javascript:(function () { " +
                        " var w = " + Webpage.getWidth(NewsView.this) + ";" +
                        " for( var i = 0; i < document.images.length; i++ ) {" +
                        " var img = document.images[i]; " +
                        "   img.height = Math.round( img.height * ( w/img.width ) ); " +
                        "   img.width = w; " +
                        " }" +
                        " })();";

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        webView.loadUrl(js);
                    }
                });

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
