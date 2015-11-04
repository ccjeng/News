package com.ccjeng.news.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsListAdapter;
import com.ccjeng.news.adapter.RecyclerItemClickListener;
import com.ccjeng.news.service.rss.RSSFeed;
import com.ccjeng.news.service.rss.RSSService;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsRSSList extends AppCompatActivity {

    private static final String TAG = "NewsRSSList";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private int sourceNumber;
    private int itemNumber;
    private String tabName;
    private String categoryName;
    private String[] feedURL;
    private String rssFeedURL = null;
    //private RSSFeed mRssList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsslist);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        categoryName = bunde.getString("CategoryName");
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        tabName = bunde.getString("SourceTab");
        //set toolbar title
        getSupportActionBar().setTitle(categoryName);
        showResult(tabName, sourceNumber);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(final RSSFeed rssList) {
        //public void setListView(List<RSSItem> rssList) {

        //mRssList = rssList;
        NewsListAdapter adapter = new NewsListAdapter(this, rssList);
        recyclerView.setAdapter(adapter);

        progressWheel.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                      //  goIntent(position, category[position]);
                        //showDetail(position);

                        //open browser
                        Uri uri = Uri.parse(rssList.getItem(position).getLink());
                        startActivity( new Intent(Intent.ACTION_VIEW, uri));
                    }
                })
        );

    }

    private void showResult(String tabName, int sourceNumber) {

        progressWheel.setVisibility(View.VISIBLE);

        if (tabName.equals("TW")) {
            switch (sourceNumber) {
                case 0:
                    feedURL = getResources().getStringArray(R.array.newsfeedsYahoo);
                    break;
                case 1:
                    feedURL = getResources().getStringArray(R.array.newsfeedsUDN);
                    break;
                case 2:
                    feedURL = getResources().getStringArray(R.array.newsfeedsYam);
                    break;
                case 3:
                    feedURL = getResources().getStringArray(R.array.newsfeedsChinaTimes);
                    break;
                case 4:
                    feedURL = getResources().getStringArray(R.array.newsfeedsStorm);
                    break;
                case 5:
                    feedURL = getResources().getStringArray(R.array.newsfeedsCommercial);
                    break;
                case 6:
                    feedURL = getResources().getStringArray(R.array.newsfeedsEttoday);
                    break;
                case 7:
                    feedURL = getResources().getStringArray(R.array.newsfeedsCNYes);
                    break;
                case 8:
                    feedURL = getResources().getStringArray(R.array.newsfeedsNewsTalk);
                    break;
                case 9:
                    feedURL = getResources().getStringArray(R.array.newsfeedsLibertyTimes);
                    break;
                case 10:
                    feedURL = getResources().getStringArray(R.array.newsfeedsAppDaily);
                    break;
            }
        } else if (tabName.equals("HK")) {
            switch (sourceNumber) {
                case 0:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKAppleDaily);
                    break;
                case 1:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKOrientalDaily);
                    break;
                case 2:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKYahoo);
                    break;
                case 3:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKMingRT);
                    break;
                case 4:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKMing);
                    break;
                case 5:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKEJ);
                    break;
                case 6:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKMetro);
                    break;
                case 7:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKsun);
                    break;
                case 8:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKam730);
                    break;
                case 9:
                    feedURL = getResources().getStringArray(R.array.newsfeedsHKheadline);
                    break;
            }
        }

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
            //showError();
            //return;
        }


    }

    private void showDetail(int position) {

        Intent intent = new Intent();
        intent.setClass(NewsRSSList.this, NewsWeb.class);

        Bundle bundle = new Bundle();
        bundle.putString("SourceNum", Integer.toString(sourceNumber));
        bundle.putString("SourceTab", tabName);
        bundle.putString("position", Integer.toString(position));
    //    bundle.putString("url", mRssList.getItem(position).getLink());

        //itemintent.putExtras(bundle);
        //itemintent.putExtra("feed", info);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
