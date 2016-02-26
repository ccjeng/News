package com.ccjeng.news.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ccjeng.news.News;
import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsListAdapter;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSService;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mopub.mobileads.MoPubView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class NewsRSSList extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NewsRSSList";
    private Analytics ga;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

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
        //setContentView(R.layout.activity_rsslist);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_rsslist)
                .setSwipeBackView(R.layout.swipeback)
                .setSwipeBackContainerBackgroundColor(Color.TRANSPARENT);

        ButterKnife.bind(this);

        ga = new Analytics();
        ga.trackerPage(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_keyboard_backspace)
                .color(Color.WHITE)
                .actionBar());

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
            Crouton.makeText(NewsRSSList.this, R.string.network_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.croutonview)).show();
        }

        moPubView = (MoPubView) findViewById(R.id.adview);
        Network.AdView(this, moPubView, Constant.Ad_MoPub_RSS);

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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
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
            Crouton.makeText(NewsRSSList.this, R.string.data_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.croutonview)).show();

        } else {
        //response Success
            NewsListAdapter adapter = new NewsListAdapter(this, rssList);
            recyclerView.setAdapter(adapter);

            if (rssList.getItemCount() == 0) {
                Crouton.makeText(NewsRSSList.this, R.string.no_data, Style.CONFIRM,
                        (ViewGroup) findViewById(R.id.croutonview)).show();
            }

            progressWheel.setVisibility(View.GONE);

            /*
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            showDetail(position, rssList);
                        }
                    })
            );*/
        }


    }

    private void showResult(String tabName, int sourceNumber) {

        progressWheel.setVisibility(View.VISIBLE);

        Category cat = new Category(this);
        feedURL = cat.getFeedURL(tabName, sourceNumber);

        //get RSS Feed
        if (feedURL != null) {
            rssFeedURL = feedURL[itemNumber];

            try {
                final URL feedURL = new URL(rssFeedURL);
                RSSService srv = new RSSService();
                srv.requestRSS(NewsRSSList.this, feedURL);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getFeed error = " + e.toString());
            }


        }

        if (rssFeedURL == null) {
            Crouton.makeText(NewsRSSList.this, R.string.network_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.croutonview)).show();
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
