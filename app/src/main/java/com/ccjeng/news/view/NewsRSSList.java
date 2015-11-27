package com.ccjeng.news.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.ccjeng.news.adapter.RecyclerItemClickListener;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.controler.rss.RSSService;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Network;
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

public class NewsRSSList extends AppCompatActivity {

    private static final String TAG = "NewsRSSList";
    //private Analytics ga;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private MoPubView moPubView;

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

        //ga = new Analytics();
        //ga.trackerPage(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_keyboard_backspace)
                .color(Color.WHITE)
                .actionBarSize());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

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

        if (Network.isNetworkAvailable(this)) {
            showResult(tabName, sourceNumber);
        } else {
            Crouton.makeText(NewsRSSList.this, R.string.network_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.croutonview)).show();
        }


        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(News.AD_MoPub);
        moPubView.loadAd();

    }


    @Override
    protected void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }
        super.onDestroy();
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

        NewsListAdapter adapter = new NewsListAdapter(this, rssList);
        recyclerView.setAdapter(adapter);

        if (rssList.getItemCount() == 0) {
            Crouton.makeText(NewsRSSList.this, R.string.no_data, Style.CONFIRM,
                    (ViewGroup) findViewById(R.id.croutonview)).show();
        }

        progressWheel.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showDetail(position, rssList);
                    }
                })
        );

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

    private void showDetail(int position, RSSFeed rssList) {

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
