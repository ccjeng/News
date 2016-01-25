package com.ccjeng.news.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ccjeng.news.News;
import com.ccjeng.news.R;
import com.ccjeng.news.controler.web.NewsHandler;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.utils.PreferenceSetting;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mopub.mobileads.MoPubView;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

//todo 加上新聞小幫手檢核 sample: https://github.com/g0v/newshelper-extension/blob/master/background.js
//todo add AD banner

public class NewsView extends AppCompatActivity {

    private static final String TAG = "NewsView";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    public WebView webView;
    @Bind(R.id.progress_wheel)
    public ProgressWheel progressWheel;
    @Bind(R.id.main)
    public NestedScrollView main;

    private Analytics ga;

    private String newsName;
    private String newsUrl;
    private String newsTitle;
    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_web)
                .setSwipeBackView(R.layout.swipeback)
                .setSwipeBackContainerBackgroundColor(Color.TRANSPARENT);

        ButterKnife.bind(this);

        ga = new Analytics();
        ga.trackerPage(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_keyboard_backspace)
                .color(Color.WHITE)
                .actionBar());

        PreferenceSetting.getPreference(this);

        Bundle bundle = this.getIntent().getExtras();
        final int sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
        final String tabName = bundle.getString("SourceTab");
        newsName = bundle.getString("NewsName");
        final String categoryName = bundle.getString("CategoryName");

        newsUrl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newsTitle");

        getSupportActionBar().setTitle(newsTitle);
        getSupportActionBar().setSubtitle(newsName);

        main.setBackgroundColor(Color.parseColor(News.getPrefBGColor()));

        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(News.AD_MoPub);


        if (Network.isNetworkConnected(this)) {

            progressWheel.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);
            NewsHandler.getNewsContent(this, newsUrl, tabName, sourceNumber);

            moPubView.loadAd();

        } else {
            Crouton.makeText(NewsView.this, R.string.network_error, Style.ALERT,
                    (ViewGroup) findViewById(R.id.croutonview)).show();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        PreferenceSetting.getPreference(this);
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
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);

        MenuItem menuItem1 = menu.findItem(R.id.action_browser);
        menuItem1.setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_web).actionBar().color(Color.WHITE));

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
            case R.id.action_setting:
                startActivity(new Intent(NewsView.this, Preference.class));
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
