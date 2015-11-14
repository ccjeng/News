package com.ccjeng.news.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsCategoryAdapter;
import com.ccjeng.news.adapter.RecyclerItemClickListener;
import com.ccjeng.news.utils.Category;
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
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        categoryName = bunde.getString("SourceName");
        tabName = bunde.getString("SourceTab");

        //set toolbar title
        getSupportActionBar().setTitle(categoryName);
        showResult(tabName, sourceNumber);
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
