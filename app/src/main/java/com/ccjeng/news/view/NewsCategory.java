package com.ccjeng.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsCategoryAdapter;
import com.ccjeng.news.adapter.RecyclerItemClickListener;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.view.base.BaseActivity;
import com.mopub.mobileads.MoPubView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsCategory extends BaseActivity {

    private static final String TAG = NewsCategory.class.getName();
    private Analytics ga;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private MoPubView moPubView;
    private int sourceNumber;
    private String tabName;
    private String categoryName;
    private String[] category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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

        //get intent values
        Bundle bunde = this.getIntent().getExtras();
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        categoryName = bunde.getString("SourceName");
        tabName = bunde.getString("SourceTab");

        //set toolbar title
        getSupportActionBar().setTitle(categoryName);
        showResult(tabName, sourceNumber);


        moPubView = (MoPubView) findViewById(R.id.adview);
        Network.AdView(moPubView, Constant.Ad_MoPub_Category);

        ga.trackEvent(this, "Click", "News", categoryName, 0);

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

    private void showResult(String tabName, int sourceNumber) {

        Category cat = new Category(this);
        category = cat.getCategory(tabName, sourceNumber);

        if (category != null) {
            NewsCategoryAdapter adapter = new NewsCategoryAdapter(this, category);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            goIntent(position, category[position]);
                        }
                    })
            );

        }
    }

    private void goIntent(int itemnumber, String itemname) {
        Intent intent = new Intent();
        intent.setClass(NewsCategory.this, NewsRSSList.class);
        Bundle bundle = new Bundle();

        bundle.putString("CategoryNum", Integer.toString(itemnumber));
        bundle.putString("CategoryName", itemname);
        bundle.putString("NewsName", categoryName);
        bundle.putString("SourceNum", Integer.toString(sourceNumber));
        bundle.putString("SourceTab", tabName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
