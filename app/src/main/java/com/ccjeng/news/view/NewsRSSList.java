package com.ccjeng.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsListAdapter;
import com.ccjeng.news.controler.rss.IRSSCallback;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSService;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.utils.UI;
import com.ccjeng.news.view.base.BaseActivity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.mopub.mobileads.MoPubView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsRSSList extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = NewsRSSList.class.getSimpleName();
    private Analytics ga;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;

    private MoPubView moPubView;
    private SwipeRefreshLayout mSwipeLayout;

    private int sourceNumber;
    private int itemNumber;
    private String tabName;
    private String newsName;
    private String categoryName;
    private String[] feedURL;
    private String rssFeedURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsslist);
        ButterKnife.bind(this);

        ga = new Analytics();
        ga.trackerPage(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //get intent values
        Bundle bunde = this.getIntent().getExtras();
        itemNumber = Integer.parseInt(bunde.getString("CategoryNum"));
        newsName = bunde.getString("NewsName");
        categoryName = bunde.getString("CategoryName");
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        tabName = bunde.getString("SourceTab");

        //set toolbar title
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setSubtitle(newsName);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        if (Network.isNetworkConnected(this)) {
            showResult(tabName, sourceNumber);
        } else {
            UI.showErrorSnackBar(coordinator, R.string.network_error);
        }


        moPubView = (MoPubView) findViewById(R.id.adview);
        Network.AdView(moPubView, Constant.Ad_MoPub_RSS);

        ga.trackEvent(this, "Click", "Category", newsName + "-" + categoryName, 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }
        super.onDestroy();
    }


/* SwipeRefreshLayout
* */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //refresh data
                showResult(tabName, sourceNumber);
                mSwipeLayout.setRefreshing(false);
            }
        }, 3000);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(final RSSFeed rssList) {

        if (rssList == null) {
        //response Error
            progressWheel.setVisibility(View.GONE);
            UI.showErrorSnackBar(coordinator, R.string.data_error);

        } else {
        //response Success
            NewsListAdapter adapter = new NewsListAdapter(this, rssList);
            recyclerView.setAdapter(adapter);

            if (rssList.getItemCount() == 0) {
                UI.showErrorSnackBar(coordinator, R.string.no_data);
            }

            progressWheel.setVisibility(View.GONE);

        }


    }

    private void showResult(String tabName, int sourceNumber) {

        progressWheel.setVisibility(View.VISIBLE);

        Category cat = new Category(this);
        feedURL = cat.getFeedURL(tabName, sourceNumber);

        //get RSS Feed
        if (feedURL != null) {
            rssFeedURL = feedURL[itemNumber];


            IRSSCallback callback = new IRSSCallback() {
                @Override
                public void onRSSReceived(final RSSFeed rssFeed) {
                    setListView(rssFeed);
                }
                @Override
                public void onRSSFailed(String error) {
                    Log.e(TAG, error);
                    UI.showErrorSnackBar(coordinator, R.string.data_error);
                }
            };


            try {
                URL feedURL = new URL(rssFeedURL);

                RSSService srv = new RSSService(feedURL, callback);
                srv.requestRSS();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getFeed error = " + e.toString());
            }


        }

        if (rssFeedURL == null) {
            UI.showErrorSnackBar(coordinator, R.string.network_error);
        }


    }

    public void showDetail(int position, RSSFeed rssList) {

        Intent intent = new Intent();
        intent.setClass(NewsRSSList.this, NewsView.class);

        Bundle bundle = new Bundle();
        bundle.putString("SourceNum", Integer.toString(sourceNumber));
        bundle.putString("SourceTab", tabName);
        bundle.putString("NewsName", newsName);
        bundle.putString("CategoryName", categoryName);
        bundle.putString("position", Integer.toString(position));
        bundle.putString("newsUrl", rssList.getItem(position).getLink());
        bundle.putString("newsTitle", rssList.getItem(position).getTitle());

        //itemintent.putExtras(bundle);
        //itemintent.putExtra("feed", info);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
