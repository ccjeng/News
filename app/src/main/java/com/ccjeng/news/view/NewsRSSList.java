package com.ccjeng.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.presenter.NewsRSSListPresenter;
import com.ccjeng.news.presenter.NewsRSSListView;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.utils.UI;
import com.ccjeng.news.view.adapter.NewsListAdapter;
import com.ccjeng.news.view.base.MVPBaseActivity;
import com.mopub.mobileads.MoPubView;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsRSSList extends MVPBaseActivity<NewsRSSListView, NewsRSSListPresenter>
        implements NewsRSSListView {

    private static final String TAG = NewsRSSList.class.getSimpleName();
    private Analytics ga;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    private MoPubView moPubView;
    private SwipeRefreshLayout mSwipeLayout;

    private int sourceNumber;
    private int itemNumber;
    private String tabName;
    private String newsName;
    private String categoryName;

    private NewsListAdapter adapter;

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

        adapter = new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);

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
        mSwipeLayout.setOnRefreshListener(mPresenter);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        if (Network.isNetworkConnected(this)) {
            this.refreshData();
        } else {
            this.showError(R.string.network_error);
        }


        moPubView = (MoPubView) findViewById(R.id.adview);
        Network.AdView(moPubView, Constant.Ad_MoPub_RSS);

        ga.trackEvent(this, "Click", "Category", newsName + "-" + categoryName, 0);

    }


    @Override
    protected void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }
        mPresenter.onDestroy();
        super.onDestroy();
    }


    @Override
    protected NewsRSSListPresenter createPresenter() {
        return new NewsRSSListPresenter(this, this);
    }

    @Override
    public void refreshData() {
        progressWheel.setVisibility(View.VISIBLE);
        mPresenter.getRSSData(tabName, sourceNumber, itemNumber);
        mSwipeLayout.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void setListView(RSSFeed rssList) {

        if (rssList == null) {
        //response Error
            progressWheel.setVisibility(View.GONE);
            this.showError(R.string.data_error);

        } else {
        //response Success
            adapter.setData(rssList);

            if (rssList.getItemCount() == 0) {
                this.showError(R.string.no_data);
            }
            progressWheel.setVisibility(View.GONE);

        }
    }

    @Override
    public void showError(int message) {
        UI.showErrorSnackBar(coordinator, message);
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

        intent.putExtras(bundle);
        startActivity(intent);
    }

}
