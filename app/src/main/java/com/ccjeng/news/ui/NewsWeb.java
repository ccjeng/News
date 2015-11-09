package com.ccjeng.news.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ccjeng.news.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsWeb extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    XWalkView webView;

    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_keyboard_backspace)
                .color(Color.WHITE)
                .actionBarSize());

        Bundle bundle = this.getIntent().getExtras();
        int sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
        String TabName = bundle.getString("SourceTab");
        String newsName = bundle.getString("NewsName");
        String categoryName = bundle.getString("CategoryName");

        newsUrl = bundle.getString("newsUrl");
        String newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        webView.load(newsUrl, null);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);


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
                openBrowser();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void openBrowser() {

        Uri uri = Uri.parse(newsUrl);
        startActivity( new Intent(Intent.ACTION_VIEW, uri));

    }
}
