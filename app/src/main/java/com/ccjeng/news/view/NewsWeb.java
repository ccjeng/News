package com.ccjeng.news.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ccjeng.news.R;
import com.ccjeng.news.utils.Network;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class NewsWeb extends AppCompatActivity {

    private static final String TAG = "NewsWeb";

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
        final String TabName = bundle.getString("SourceTab");
        newsName = bundle.getString("NewsName");
        final String categoryName = bundle.getString("CategoryName");

        newsUrl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        getPrefs();

        if (Network.isNetworkAvailable(this)) {
            webView.loadUrl(newsUrl, null);
            //webView.getSettings().setJavaScriptEnabled(true);
            //webView.getSettings().setBuiltInZoomControls(true);
        } else {
            Crouton.makeText(NewsWeb.this, R.string.network_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.main)).show();
        }



        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted");
                progressWheel.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished");
                progressWheel.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressWheel.setProgress((float) newProgress / 100);

                if (newProgress > 90) {
                    progressWheel.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });

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

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean disableJS = prefs.getBoolean("disableJS", false);

        Log.d(TAG, "disableJS = " + disableJS);

        if (disableJS) {
            webView.getSettings().setJavaScriptEnabled(false);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
