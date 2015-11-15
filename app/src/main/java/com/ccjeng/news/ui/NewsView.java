package com.ccjeng.news.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.ccjeng.news.R;
import com.ccjeng.news.service.web.NewsHandler;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsView extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private String newsName;
    private String newsUrl;
    private String newsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_keyboard_backspace)
                .color(Color.WHITE)
                .actionBarSize());

        Bundle bundle = this.getIntent().getExtras();
        final int sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
        final String tabName = bundle.getString("SourceTab");
        newsName = bundle.getString("NewsName");
        final String categoryName = bundle.getString("CategoryName");

        newsUrl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        NewsHandler.getNewsContent(this, newsUrl, tabName, sourceNumber);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);

        MenuItem menuItem1 = menu.findItem(R.id.action_browser);
        menuItem1.setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_web).actionBarSize().color(Color.WHITE));

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
        }

        return super.onOptionsItemSelected(item);
    }


    private void menuOpenBrowser() {

        Uri uri = Uri.parse(newsUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

    private void menuShare() {

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "["+ newsName + "] " + newsTitle + " " + newsUrl);
        startActivity(Intent.createChooser(intent, getString(R.string.sharing_description)));

    }



}
