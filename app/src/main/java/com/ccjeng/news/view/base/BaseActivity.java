package com.ccjeng.news.view.base;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;

import me.majiajie.swipeback.SwipeBackActivity;

/**
 * Created by andycheng on 2016/6/17.
 */
public class BaseActivity  extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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
}
