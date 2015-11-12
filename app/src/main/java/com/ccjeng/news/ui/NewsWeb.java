package com.ccjeng.news.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.ccjeng.news.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsWeb extends AppCompatActivity {

    private static final String TAG = "NewsWeb";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    XWalkView webView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

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
        final int sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
        final String TabName = bundle.getString("SourceTab");
        final String newsName = bundle.getString("NewsName");
        final String categoryName = bundle.getString("CategoryName");

        newsUrl = bundle.getString("newsUrl");
        final String newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        webView.load(newsUrl, null);
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, false);
        //XWalkPreferences.setValue("enable-javascript",false);
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        final Activity WebActivity = this;

        webView.setUIClient(new XWalkUIClient(webView) {

            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                Log.d(TAG, "onPageLoadStarted");
                progressWheel.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
                Log.d(TAG, "onPageLoadStopped");
                progressWheel.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });

        webView.setResourceClient(new XWalkResourceClient(webView) {
            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);

                progressWheel.setProgress((float) progressInPercent / 100);

                if (progressInPercent > 95) {
                    progressWheel.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });

        //webView.load("javascript:document.body.style.transform=\"scale(ZoomLevel)\";", null);

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
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.pauseTimers();
            webView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.resumeTimers();
            webView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.onDestroy();
        }
    }
}
