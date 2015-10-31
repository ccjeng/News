package com.ccjeng.news;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.news.adapter.NewsCategoryAdapter;
import com.ccjeng.news.adapter.RecyclerItemClickListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsCategory extends AppCompatActivity {

    private static final String TAG = "NewsCategory";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private int sourceNumber;
    private String tabName;
    private String categoryName;
    private String[] category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        categoryName = bunde.getString("SourceName");
        tabName = bunde.getString("SourceTab");

        //set toolbar title
        getSupportActionBar().setTitle(categoryName);
        showResult(tabName, sourceNumber);
        //Log.d(TAG, tabName +" " + CategoryName);
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

    private void showResult(String tabName, int sourceNumber) {

        if (tabName.equals("TW")) {
            switch (sourceNumber) {
                case 0:
                    category = getResources().getStringArray(R.array.newscatsYahoo);
                    break;
                case 1:
                    category = getResources().getStringArray(R.array.newscatsUDN);
                    break;
                case 2:
                    category = getResources().getStringArray(R.array.newscatsPCHome);
                    break;
                case 3:
                    category = getResources().getStringArray(R.array.newscatsChinaTimes);
                    break;
                case 4:
                    category = getResources().getStringArray(R.array.newscatsNOW);
                    break;
                case 5:
                    category = getResources().getStringArray(R.array.newscatsEngadget);
                    break;
                case 6:
                    category = getResources().getStringArray(R.array.newscatsBNext);
                    break;
                case 7:
                    category = getResources().getStringArray(R.array.newscatsEttoday);
                    break;
                case 8:
                    category = getResources().getStringArray(R.array.newscatsCNYes);
                    break;
                case 9:
                    category = getResources().getStringArray(R.array.newscatsNewsTalk);
                    break;
                case 10:
                    category = getResources().getStringArray(R.array.newscatsLibertyTimes);
                    break;
                case 11:
                    category = getResources().getStringArray(R.array.newscatsAppDaily);
                    break;
            }
        } else if (tabName.equals("HK")) {
            switch (sourceNumber) {
                case 0:
                    category = getResources().getStringArray(R.array.newscatsHKAppleDaily);
                    /*Category appCategory = new Category();
	        		category = appCategory.getCategory();*/
                    break;
                case 1:
                    category = getResources().getStringArray(R.array.newscatsHKOrientalDaily);
                    break;
                case 2:
                    category = getResources().getStringArray(R.array.newscatsHKYahoo);
                    break;
                case 3:
                    category = getResources().getStringArray(R.array.newscatsHKYahooStGlobal);
                    break;
                case 4:
                    category = getResources().getStringArray(R.array.newscatsHKMing);
                    break;
                case 5:
                    category = getResources().getStringArray(R.array.newscatsHKYahooMing);
                    break;
                case 6:
                    category = getResources().getStringArray(R.array.newscatsHKEJ);
                    break;
                case 7:
                    category = getResources().getStringArray(R.array.newscatsHKMetro);
                    break;
                case 8:
                    category = getResources().getStringArray(R.array.newscatsHKsun);
                    break;
                case 9:
                    category = getResources().getStringArray(R.array.newscatsHKam730);
                    break;
                case 10:
                    category = getResources().getStringArray(R.array.newscatsHKheadline);
                    break;
            }
        }

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
        bundle.putString("CategoryName", categoryName + " - " + itemname);
        bundle.putString("SourceNum", Integer.toString(sourceNumber));
        bundle.putString("SourceTab", tabName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
